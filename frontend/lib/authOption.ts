import { NextAuthOptions } from "next-auth";
import CredentialsProvider from "next-auth/providers/credentials";
import { jwtDecode } from "jwt-decode";
import { JWTPayload } from "@/types/auth";

const API_BASE_URL = process.env.API_BASE_URL;

export const authOptions: NextAuthOptions = {
  providers: [
    CredentialsProvider({
      name: "Credentials",
      credentials: {
        username: { label: "Username", type: "text" },
        password: { label: "Password", type: "password" },
      },
      async authorize(credentials) {
        if (!credentials) return null;

        const formData = new URLSearchParams();
        formData.append("username", credentials.username);
        formData.append("password", credentials.password);

        try {
          const res = await fetch(`${API_BASE_URL}/api/auth/authenticate`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Accept: "application/json",
            },
            body: JSON.stringify({
              username: formData.get("username"),
              password: formData.get("password"),
            }),
          });

          if (!res.ok) return null;

          const data = await res.json();
          const decoded = jwtDecode<JWTPayload>(data.access_token);

          if (!decoded || !decoded.sub) return null;

          return {
            id: decoded.sub,
            accessToken: data.access_token,
            refreshToken: data.refresh_token,
            username: decoded.sub,
          };
        } catch (error) {
          console.error("Authorization error:", error);
          return null;
        }
      },
    }),
  ],

  pages: {
    signIn: "/login",
  },

  callbacks: {
    async jwt({ token, user }) {
      if (user) {
        const { accessToken, username } = user as unknown as {
          accessToken: string;
          username: string;
        };

        const decoded = jwtDecode<JWTPayload>(accessToken);
        token.accessToken = accessToken;
        token.username = username;
        token.exp = decoded.exp;
        token.sub = decoded.sub;
      }

      return token;
    },

    async session({ session, token }) {
      session.user = {
        username: token.username as string,
        accessToken: token.accessToken as string,
      };
      return session;
    },
  },

  session: {
    strategy: "jwt",
  },

  secret: process.env.NEXTAUTH_SECRET,
};
