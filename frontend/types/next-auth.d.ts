import "next-auth";
import "next-auth/jwt";

declare module "next-auth" {
  interface Session {
    user: {
      username: string;
      accessToken: string;
    };
  }
}

declare module "next-auth/jwt" {
  interface JWT {
    username: string;
    accessToken: string;
    refreshToken?: string;
    exp?: number;
    sub?: string;
  }
}
