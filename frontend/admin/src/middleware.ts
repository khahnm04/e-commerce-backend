import { NextResponse, NextRequest } from 'next/server'

/**
 * Decode JWT payload (không cần signerKey, chỉ đọc expiration)
 * Việc verify signature thực sự được xử lý ở backend
 */
function isTokenExpired(token: string): boolean {
  try {
    const parts = token.split('.');
    if (parts.length !== 3) return true;

    const payload = JSON.parse(
      Buffer.from(parts[1], 'base64url').toString('utf-8')
    );

    if (!payload.exp) return true;

    return payload.exp * 1000 < Date.now();
  } catch {
    return true;
  }
}

export function middleware(request: NextRequest) {
  const accessToken = request.cookies.get('access_token')?.value;

  // Không có access token cookie → chưa đăng nhập → redirect login
  if (!accessToken) {
    return NextResponse.redirect(new URL('/login', request.url));
  }

  // Access token chưa hết hạn → cho qua
  if (!isTokenExpired(accessToken)) {
    return NextResponse.next();
  }

  // Access token đã hết hạn nhưng cookie vẫn tồn tại →
  // Cho qua để client-side api-client.ts tự động gọi refresh token.
  // Lý do: refresh_token cookie có path="/api/v1/auth", middleware không thể
  // đọc được cookie này. Chỉ browser mới gửi refresh_token khi call tới
  // /api/v1/auth/refresh. Nếu refresh cũng thất bại → api-client.ts sẽ
  // redirect về /login.
  return NextResponse.next();
}

export const config = {
  matcher: [
    /*
     * Match tất cả các route trừ:
     * - /login (trang đăng nhập)
     * - /_next (Next.js internal)
     * - /assets (static assets)
     * - /favicon.ico
     */
    '/((?!login|_next|assets|favicon\\.ico).*)',
  ],
}
