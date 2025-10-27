package util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
	// Mã hóa mật khẩu với BCrypt
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));  // 12 là số vòng băm
    }

    // Kiểm tra xem mật khẩu gốc có khớp với mật khẩu mã hóa không
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
