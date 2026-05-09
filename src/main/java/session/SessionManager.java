package session;

import dto.NhanVien_DTO;
import dto.TaiKhoan_DTO;

/**
 * Holds the logged-in staff for client UI code. Cleared on logout.
 */
public final class SessionManager {

	private static NhanVien_DTO currentNhanVien;

	private SessionManager() {
	}

	public static void setCurrentNhanVien(NhanVien_DTO nhanVien) {
		currentNhanVien = nhanVien;
	}

	public static NhanVien_DTO getCurrentNhanVien() {
		return currentNhanVien;
	}

	public static String getCurrentMaNV() {
		return currentNhanVien == null ? null : currentNhanVien.getMaNV();
	}

	/** Prefer {@code taiKhoan.maNhanVien}, fall back to session snapshot. */
	public static String resolveMaNV(TaiKhoan_DTO taiKhoan) {
		if (taiKhoan != null && taiKhoan.getMaNhanVien() != null && !taiKhoan.getMaNhanVien().isBlank()) {
			return taiKhoan.getMaNhanVien();
		}
		return getCurrentMaNV();
	}

	public static void clear() {
		currentNhanVien = null;
	}
}
