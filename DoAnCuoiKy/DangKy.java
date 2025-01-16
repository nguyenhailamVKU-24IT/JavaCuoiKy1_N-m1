package DoAnCuoiKy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class DangKy {
	private Connection connection;
	public DangKy() {
		JFrame dangKyFrame = new JFrame("Quản Lý Vật Liệu Xây Dựng");
		dangKyFrame.setSize(600, 300);
		dangKyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dangKyFrame.setResizable(false);
		dangKyFrame.setLocationRelativeTo(null);
		
		JPanel leftJPanel = new JPanel();
		leftJPanel.setLayout(new BoxLayout(leftJPanel, BoxLayout.Y_AXIS));
		leftJPanel.setBackground(new Color(32, 178, 170));
		leftJPanel.setPreferredSize(new Dimension(200, 300));
		
		JLabel iconLabel = new JLabel(new ImageIcon(DangKy.class.getResource("icon_nguoi.png")));
		iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		leftJPanel.add(iconLabel);
		
		JLabel chuJLabel = new JLabel("Quản Lý Vật Liệu Xây Dựng");
		chuJLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		leftJPanel.add(chuJLabel);
		dangKyFrame.add(leftJPanel, BorderLayout.WEST);
		
		JPanel rightJPanel = new JPanel();
		rightJPanel.setLayout(null);
		
		JLabel taoTaiKhoanMoiJLabel = new JLabel("Đăng Ký Tài Khoản");
		taoTaiKhoanMoiJLabel.setBounds(125, 10, 150, 20);
		taoTaiKhoanMoiJLabel.setFont(new Font("Arial", Font.BOLD, 16));
		rightJPanel.add(taoTaiKhoanMoiJLabel);
		
		JLabel taikhoanJLabel = new JLabel("Tài Khoản:");
		taikhoanJLabel.setBounds(100, 40, 90, 30);
		rightJPanel.add(taikhoanJLabel);
		
		JTextField taikhoanField = new JTextField();
		taikhoanField.setBounds(100, 65, 200, 28);
		rightJPanel.add(taikhoanField);
		
		JLabel matkhauJLabel = new JLabel("Mật Khẩu:");
		matkhauJLabel.setBounds(100, 90, 80, 30);
		rightJPanel.add(matkhauJLabel);
		
		JPasswordField matKhauField = new JPasswordField();
		matKhauField.setBounds(100, 115, 200, 28);
		rightJPanel.add(matKhauField);
		
		JLabel nhapLaiMatKhauJLabel = new JLabel("Nhập Lại Mật Khẩu:");
		nhapLaiMatKhauJLabel.setBounds(100, 140, 150, 30);
		rightJPanel.add(nhapLaiMatKhauJLabel);
		
		JPasswordField nhaplaimatkhauField = new JPasswordField();
		nhaplaimatkhauField.setBounds(100, 170, 200, 28);
		rightJPanel.add(nhaplaimatkhauField);
		
		JButton dangkyButton = new JButton("Đăng Ký");
		dangkyButton.setBounds(150, 200, 95, 30);
		rightJPanel.add(dangkyButton);
		
		String url = "jdbc:mysql://localhost:3306/quanLyVatLieuXayDung";
		String user = "root";
		String password = "Hailam123@";
		try {
		    connection = DriverManager.getConnection(url, user, password);
		    System.out.println("Kết nối thành công!");
		} catch (SQLException e) {
		    System.out.println("Kết nối thất bại: " + e.getMessage());
		    e.printStackTrace();
		}
		
		  dangkyButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                String taiKhoanMoi = taikhoanField.getText().trim();
	                String matKhauMoi = new String(matKhauField.getPassword()).trim();
	                String nhapLaiMK = new String(nhaplaimatkhauField.getPassword()).trim();

	                if (taiKhoanMoi.isEmpty() || matKhauMoi.isEmpty() || nhapLaiMK.isEmpty()) {
	                    JOptionPane.showMessageDialog(dangKyFrame, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	                    return;
	                }

	                if (!matKhauMoi.equals(nhapLaiMK)) {
	                    JOptionPane.showMessageDialog(dangKyFrame, "Mật khẩu không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	                    return;
	                }
	                
	                String checkSQL = "SELECT * FROM nguoiDung WHERE BINARY taiKhoan = ?";
	                try (PreparedStatement preparedStatement = connection.prepareStatement(checkSQL)) {
						preparedStatement.setString(1, taiKhoanMoi);
						ResultSet rs = preparedStatement.executeQuery();
						if (rs.next()) {
							JOptionPane.showMessageDialog(null, "Tài khoản đã tồn tại!", "Lỗi", JOptionPane.WARNING_MESSAGE);
							return;
						}
					} catch (SQLException ex) {
						// TODO: handle exception
						JOptionPane.showMessageDialog(null, "Lỗi kết nối cơ sở dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
					}

//	                try {
	                    String sql = "INSERT INTO nguoiDung (taiKhoan, matKhau) VALUES (?, ?)";
	                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	                        preparedStatement.setString(1, taiKhoanMoi);
	                        preparedStatement.setString(2, matKhauMoi);

	                        int rowsInserted = preparedStatement.executeUpdate();
	                        if (rowsInserted > 0) {
	                            JOptionPane.showMessageDialog(null, "Đăng ký thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
	                            dangKyFrame.dispose();
	                            new DangNhap();
	                        }
	                } catch (SQLException ex) {
	                        JOptionPane.showMessageDialog(null, "Lỗi khi thêm tài khoản: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	                        ex.printStackTrace();
	                }
	            }
	        });
		
		JLabel bandacotaikhoanJLabel = new JLabel("Bạn Đã Có Tài Khoản?");
		bandacotaikhoanJLabel.setBounds(70, 240, 135, 30);
		rightJPanel.add(bandacotaikhoanJLabel);
		
		JButton dangnhapButton = new JButton("Đăng Nhập");
		dangnhapButton.setBounds(220, 240, 95, 30);
		rightJPanel.add(dangnhapButton);
		
		dangnhapButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dangKyFrame.dispose();
				new DangNhap();
			}
		});
		
		dangKyFrame.add(rightJPanel);
		
		dangKyFrame.setVisible(true);
	}
	

}
