package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RootController implements Initializable{
	public Stage primaryStage;
	@FXML private ImageView imageView;
	@FXML private TextField textId;
	@FXML private PasswordField textPassword;
	@FXML private Button btnLogin;
	@FXML private Button btnClose;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//id,pw ����� �Է��ϰ� ����Ű�� �α��ι�ư Ŭ���� ���ο� â�� ����.
		imageView.setImage(new Image(getClass().getResource("../images/loginbook.jpg").toString()));
		textPassword.setOnKeyPressed(e -> { if(e.getCode().equals(KeyCode.ENTER)) {handlerBtnLoginAction();} });
		btnLogin.setOnAction(e ->  handlerBtnLoginAction() );
		//��ҹ�ư Ŭ���� â�� �ݴ´�.
		btnClose.setOnAction(e -> Platform.exit() );
		//�̹��� Ŭ���� ���̵�� ��й�ȣ�� �ڵ����� ���ش�.
		imageView.setOnMouseClicked( e -> { handleImageViewAction(e); });
	}
	//id,pw ����� �Է��ϰ� ����Ű�� �α��ι�ư Ŭ���� ���ο� â�� ����.
	private void handlerBtnLoginAction() {
		if(!(textId.getText().equals("root") && textPassword.getText().equals("123456"))) {
			textId.clear();
			textPassword.clear();
			MainController.callAlert("�α��� ���� : ���̵�� �н����带 Ȯ�����ּ���.");
			return;
		}
		try {
			Stage mainStage= new Stage();
			FXMLLoader loader= new FXMLLoader(getClass().getResource("../View/main.fxml"));
			Parent root= loader.load();
			MainController mainController= loader.getController();
			mainController.mainStage=mainStage;
			MainController.callAlert("�α��� ���� : �α��� �����ϼ̽��ϴ�.");
			mainStage.setTitle("BookList");
			Scene scene= new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../application/main.css").toString());
			mainStage.setScene(scene);
			primaryStage.close();
			mainStage.show();
			
		} catch (Exception e) {
			
		}
	}
	//�̹��� Ŭ���� ���̵�� ��й�ȣ�� �ڵ����� ���ش�.
	private void handleImageViewAction(MouseEvent e) {
		textId.setText("root");
		textPassword.setText("123456");
	}
		
}