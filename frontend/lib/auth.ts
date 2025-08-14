import { jwtDecode } from "jwt-decode";
import { JWTPayload } from "@/types/auth";

export const readNameFromJwt = (accessToken: string): string | null => {
  try {
    const decoded: JWTPayload = jwtDecode<JWTPayload>(accessToken);
    return decoded.sub;
  } catch {
    return null;
  }
};
