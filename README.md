# Quản lý kho hàng (Android, Java, SQLite)

## Cách chạy
**Cách 1 – Mở trực tiếp:** Android Studio → File → Open → chọn thư mục `QuanLyKhoHang` → đợi Gradle Sync → Run.
(Cần Android Studio Hedgehog 2023.1+ / JDK 17. Nếu Studio hỏi phiên bản Gradle, chọn 8.2 trở lên.)

**Cách 2 – Chép vào project có sẵn:** tạo project "Empty Views Activity" (Java), package `com.example.warehouse`, rồi chép:
- `app/src/main/java/com/example/warehouse/*.java` → thay vào thư mục java của package
- `app/src/main/res/layout`, `res/values` → chép đè vào `res`
- `AndroidManifest.xml` → chép đè
- `app/build.gradle` chỉ cần dependency `androidx.appcompat:appcompat`; theme của app phải là `AppTheme` (trong `values/themes.xml`).

## Tài khoản mẫu
admin / 123456

## Cấu trúc
| File | Vai trò |
| --- | --- |
| Product.java | Lớp sản phẩm (tên, giá, SL, loại, trạng thái, mô tả) |
| DatabaseHelper.java | SQLiteOpenHelper: warehouse.db v1, bảng users + products, 9 phương thức yêu cầu |
| LoginActivity | Đăng nhập, kiểm tra bằng checkLogin() trên bảng users |
| MainActivity | Thống kê + ListView, tải lại ở onResume |
| AddProductActivity | Thêm sản phẩm, kiểm tra dữ liệu |
| ProductDetailActivity | Nhận id qua Intent, truy vấn lại DB; Nhập thêm 1 / Bán 1 / Xóa / Quay lại |
| ProductAdapter, Utils | Adapter dòng sản phẩm; định dạng tiền 1.500.000 đ |

## Danh sách kiểm tra nhanh
1. Bỏ trống hoặc sai tài khoản → báo lỗi; đúng → vào danh sách, bấm Back không quay lại đăng nhập.
2. Danh sách có 3 sản phẩm mẫu; "Bàn phím cơ" (SL 3) có nhãn Sắp hết hàng; thống kê: 3 sản phẩm, 5.350.000 đ, 1 sản phẩm sắp hết hàng.
3. Thêm sản phẩm: để trống tên / giá 0 / SL rỗng → báo lỗi; hợp lệ → xuất hiện trong danh sách.
4. Chi tiết: Nhập thêm 1, Bán 1 (SL 0 → "Hết hàng"), Xóa → thống kê cập nhật.
5. Tắt hẳn app rồi mở lại → dữ liệu vẫn còn.

Giá trị kho mẫu ban đầu: 150.000×20 + 450.000×3 + 25.000×40 = 3.000.000 + 1.350.000 + 1.000.000 = **5.350.000 đ**.
