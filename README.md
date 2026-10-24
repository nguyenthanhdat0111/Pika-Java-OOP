# Pika – Trò chơi Pikachu bằng Java Swing

## Giới thiệu

Pika là trò chơi ghép cặp hình Pikachu được xây dựng bằng **Java** và thư viện giao diện **Java Swing**, phục vụ việc tìm hiểu và thực hành **lập trình hướng đối tượng (OOP)**. Người chơi tìm hai hình giống nhau và nối chúng bằng đường đi hợp lệ để loại bỏ khỏi bàn chơi trước khi hết thời gian.

Dự án sử dụng cấu trúc nhiều lớp, tách biệt phần giao diện, điều khiển trò chơi, thuật toán xử lý ma trận và các tiện ích âm thanh.

## Chức năng chính

* Màn hình khởi động và menu chính; tùy chọn **Play**, hướng dẫn chơi và thông tin trò chơi.
* Ba mức độ **Easy / Medium / Hard** với giới hạn thời gian khác nhau khi bắt đầu lượt chơi: lần lượt **180 / 120 / 60 giây**.
* Hiển thị bảng hình, cho phép chọn hai ô và kiểm tra khả năng nối hợp lệ.
* Tính điểm khi ghép thành công, hiển thị điểm và thời gian đếm ngược.
* Nút **Hint** hỗ trợ người chơi (chức năng gợi ý/xáo trộn theo xử lý hiện tại của game).
* **Pause / Continue**, chơi lại, trở về menu và xử lý khi thắng hoặc thua.
* Hình ảnh giao diện, nhạc nền và hiệu ứng âm thanh.

## Công nghệ sử dụng

* **Java** (cấu hình biên dịch source/target: **Java 8**).
* **Java Swing / AWT** để xây dựng giao diện desktop và xử lý sự kiện.
* **Apache Ant** với cấu trúc project **NetBeans** (`build.xml`, `nbproject/`).
* Ma trận hai chiều và các hàm kiểm tra đường nối để xử lý luật ghép hình.

## 

