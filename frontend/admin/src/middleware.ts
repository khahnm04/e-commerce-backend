import { NextResponse, NextRequest } from "next/server";

export function middleware(request: NextRequest) {
  const accessToken = request.cookies.get("access_token")?.value;
  const refreshToken = request.cookies.get("refresh_token")?.value;

  // Nếu không có access_token VÀ không có refresh_token → redirect login
  // Nếu có refresh_token nhưng không có access_token → cho qua, client-side sẽ tự refresh
  if (!accessToken && !refreshToken) {
    return NextResponse.redirect(new URL("/login", request.url));
  }

  return NextResponse.next();
}

export const config = {
  matcher: ["/((?!login|_next|assets|favicon\\.ico).*)"],
};
