 package Controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import Model.Book;
import Model.BookDB;
import Model.SubBook;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable{
	public Stage mainStage;
	//main.fxml�� fx:id
	@FXML TableView<Book> b1TableView;
	@FXML ImageView b1ImageView;
	@FXML Button b1BtnRegister;
	@FXML Button b1BtnLook;
	@FXML Button b1BtnStory;
	@FXML Button b1BtnDrop;
	@FXML Button b1BtnChart;
	@FXML Button b1BtnEdit;
	@FXML Button b1BtnClose;
	@FXML Button b1BtnSearch;
	@FXML TextField b1TextSearch;
	//--------------------------------------
	private Book selectBook;				// ���̺���� ���õ� �������� ������ ����
	private int selectBookIndex;			// ���̺���� ���õ� �ε����� ������ ����
	private SubBook selectSubBook;	// ����â ���̺���� ���õ� �������� ������ ����
	private File selectFile = null;			// ���õ� �̹��������� ������ ����
	private int count=0;						// ���â���� �̹������ڸ� ������Ű�ų� ���ҽ����ִ� ����
	private Book book=null;				// ���� ���̺��� ����� ��Ŭ����
	private BookDB bookDB=null;		// �����ͺ��̽��� ���̺�� ����� ��Ŭ����
	ArrayList<Book> dbArrayList;		// �����ͺ��̽��� ���̺�� ������ ������  �ִ� ����
	String[] firstImage = null;				// �̹��� �̸��� ���ø����� ���� ���� �������� ����
	int countPage=0;							// �����ͺ��̽��� ����� �ϸ�ũ�������� �������ڿ� ���������ڸ� ������ �������ִ� ����
	int countFolder=0; 
	int folderNum=0;							// ����â ���̺���� ����Ʈ���ڸ� �������ִ� ����
	int firstNumber = 0;						// ùȸ���⸦ �������� ù��° ������ �������� 1�� �������ִ� ����
	String fileName="file:///D:/";			// ���� ����� �պκ��� ����
	String imagePath="D:/comic/";		// �̹��� ���� ����� �պκ��� ����
	private File imageDir = new File("C:/comic");											//�̹����� ������ ��ġ
	ObservableList<Book> b1ListDate= FXCollections.observableArrayList();			//���� ���̺��� ����� obsList
	ObservableList<String> regCmbGenreList= FXCollections.observableArrayList();	//�޺��ڽ��� ����� obsList(�帣) 
	ObservableList<String> regCmbFinishList= FXCollections.observableArrayList();	//�޺��ڽ��� ����� obsList(�ϰῩ��)
 	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 
		//1. ���̺�� �� ����
		setT1TableView();
		//2. ���â�� �޺��ڽ� ���� ����
		setRegComboBox();
		//3. ��Ϲ�ư�� �������� ó���ϴ� �Լ�
		b1BtnRegister.setOnAction( e -> { handleBtnRegisterAction();  });
		//4. ���̺�� Ŭ�������� ó���ϴ� �Լ�
		b1TableView.setOnMouseClicked(e -> { handleB1TableViewAction(e); });
		//5. ������ư �������� ó���ϴ� �Լ�
		b1BtnDrop.setOnAction(e -> { handleBtnDropAction();  });
		//6. ������ư �������� ó���ϴ� �Լ�
		b1BtnEdit.setOnAction(e -> { handleBtnEditAction();  });
		//7. �ݱ��ư �������� ó���ϴ� �Լ�
		b1BtnClose.setOnAction(e -> { handleBtnCloseAction();  });
		//8. �˻���ư �������� ó���ϴ� �Լ�
		b1BtnSearch.setOnAction(e -> { handleBtnSearchAction();   });
		//9. ��Ʈ��ư �������� ó���ϴ� �Լ�
		b1BtnChart.setOnAction(e -> { handleBtnChartAction();  });
		
	}

	//1. ���̺�� �� ����
	private void setT1TableView() {
		TableColumn tcNo = b1TableView.getColumns().get(0);
		tcNo.setCellValueFactory(new PropertyValueFactory<>("no"));
		tcNo.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcTitle = b1TableView.getColumns().get(1);
		tcTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		tcTitle.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcWriter = b1TableView.getColumns().get(2);
		tcWriter.setCellValueFactory(new PropertyValueFactory<>("writer"));
		tcWriter.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcScore = b1TableView.getColumns().get(3);
		tcScore.setCellValueFactory(new PropertyValueFactory<>("score"));
		tcScore.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcGenre = b1TableView.getColumns().get(4);
		tcGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
		tcGenre.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcDate = b1TableView.getColumns().get(5);
		tcDate.setCellValueFactory(new PropertyValueFactory<>("mdate"));
		tcDate.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcState = b1TableView.getColumns().get(6);
		tcState.setCellValueFactory(new PropertyValueFactory<>("state"));
		tcState.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcFinish = b1TableView.getColumns().get(7);
		tcFinish.setCellValueFactory(new PropertyValueFactory<>("finish"));
		tcFinish.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcNumber = b1TableView.getColumns().get(8);
		tcNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
		tcNumber.setStyle("-fx-alignment : CENTER;");
		
		TableColumn tcImage = b1TableView.getColumns().get(9);
		tcImage.setCellValueFactory(new PropertyValueFactory<>("image"));
		tcImage.setStyle("-fx-alignment : CENTER;");
		
		b1TableView.setItems(b1ListDate);
		//DB�� �ִ� ������ ���̺�信 �����Ų��.
		dbArrayList = BookDAO.getBookTotalData();
		for(Book book  :  dbArrayList  ) {
			b1ListDate.add(book);
		}
	}
	//2. ���â�� �޺��ڽ� ���� ����, �ʱ� ��ư ����
	private void setRegComboBox() {
		regCmbFinishList.addAll("�ϰ�","����");
		regCmbGenreList.addAll("������","�׼�","�ҳ�","������","��Ÿ��","���","���","�ڹ�");
		b1BtnLook.setDisable(true);
		b1BtnStory.setDisable(true);
		b1BtnDrop.setDisable(true);
		b1BtnEdit.setDisable(true);
	}
	//3. ��Ϲ�ư�� �������� ó���ϴ� �Լ�
	private void handleBtnRegisterAction() {
		try {
			Stage registerStage = new Stage();
			registerStage.initModality(Modality.WINDOW_MODAL);
			registerStage.initOwner(mainStage);
			FXMLLoader loader= new FXMLLoader(getClass().getResource("../View/register.fxml"));		
			Parent root = loader.load();
			
			TextField regTextNo = (TextField)root.lookup("#regTextNo");
			TextField regTextWriter = (TextField)root.lookup("#regTextWriter");
			TextField regTextImage = (TextField)root.lookup("#regTextImage");
			TextField regTextState = (TextField)root.lookup("#regTextState");
			TextField regTextTitle = (TextField)root.lookup("#regTextTitle");
			TextField regTextScore = (TextField)root.lookup("#regTextScore");
			TextField regTextNumber = (TextField)root.lookup("#regTextNumber");
			TextArea regTextStory = (TextArea)root.lookup("#regTextStory");
			ComboBox regCmbGenre = (ComboBox)root.lookup("#regCmbGenre");
			ComboBox regCmbFinish = (ComboBox)root.lookup("#regCmbFinish");
			DatePicker  regDatePickerDate = (DatePicker)root.lookup("#regDatePickerDate");
			ImageView regImageView = (ImageView)root.lookup("#regImageView");
			Button regBtnImage = (Button)root.lookup("#regBtnImage");
			Button regBtnSave = (Button)root.lookup("#regBtnSave");
			Button regBtncancle = (Button)root.lookup("#regBtncancle");
			registerStage.setTitle("Register");
			regImageView.setImage(new Image(getClass().getResource("../images/image.jpg").toString()));
			//�̹��� �ؽ�Ʈ�ʵ鿡 ���� ���� �Է� ���ϵ��� ����
			regTextImage.setEditable(false);	
			regCmbFinish.setItems(regCmbFinishList);
			regCmbGenre.setItems(regCmbGenreList);
			//��Ϲ�ȣ�� ���� 5�ڸ��� ����
			inputDecimalFormat(regTextNo);
			//�����ư �������� ���̺���� ���� ����.
			regBtnSave.setOnAction(e -> {
				try {
					//����, �۰�, ����, �̹���, �ٰŸ��� �Էµ��� ������ �˶�â�� ���� �����Ѵ�.
					if(regTextTitle.getText().equals("") || regTextWriter.getText().equals("") || regTextState.getText().equals("") || regTextImage.getText().equals("") || regTextStory.getText().equals("") ) {
						callAlert("���� �߻� : ��� �׸��� �Է��ϼ���."); 
						return;
					}
					//���̺��� ����� ��Ŭ����
					book=new Book(Integer.parseInt(regTextNo.getText()), regTextTitle.getText().toString(),
							regTextWriter.getText(), Double.parseDouble(regTextScore.getText()),
							regCmbGenre.getValue().toString(), Date.valueOf(regDatePickerDate.getValue().toString()), regTextState.getText(),
							regCmbFinish.getValue().toString(), Integer.parseInt(regTextNumber.getText()), regTextImage.getText());
					registerStage.close();
					//�����ͺ��̽� ���̺�� ����� ��Ŭ����
					bookDB=new BookDB(Integer.parseInt(regTextNo.getText()), regTextTitle.getText(),
							regTextWriter.getText(), Double.parseDouble(regTextScore.getText()),
							regCmbGenre.getValue().toString(), Date.valueOf(regDatePickerDate.getValue().toString()), regTextState.getText(),
							regCmbFinish.getValue().toString(), Integer.parseInt(regTextNumber.getText()), regTextImage.getText(),
							regTextStory.getText(),null);
					registerStage.close();
					//�����ư�� �������� ���̺��� �����ͺ��̽��� ��������ش�.
					b1ListDate.add(book);
					int count = BookDAO.insertBookData(bookDB);
					if(count!=0) {
						callAlert("��� ���� :"+regTextTitle.getText()+ "å�� ��� �Ǿ����ϴ�.");
					}					
				}catch (NumberFormatException e1) {
					callAlert("��� ���� : ��Ϲ�ȣ, ����, �Ǽ��� �Է��ϼ���.");
				}catch (NullPointerException e1) {
					callAlert("��� ���� : ��� �׸��� �Է��ϼ���.");
				}catch (Exception e1) { }
			});
			//�̹��� �����ư �������� �̹��� ������ �����ϰ� �̹��� �̸��� �ؽ�Ʈ�̹����� ��Ÿ����.
			regBtnImage.setOnAction(e -> {
				FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().add(new ExtensionFilter("Image File","*.png","*.jpg","*.gif"));
				selectFile = fileChooser.showOpenDialog(mainStage);
				String localURL=null;
				if(selectFile != null) {
					try {
						localURL = selectFile.toURI().toURL().toString();
					} catch (MalformedURLException e1) { }
				}
				//���õ� �̹��� ������ �̹����信 ��Ÿ���� �̹����ؽ�Ʈ�ʵ忡 �̹��� �̸��� ���ش�.
				regImageView.setImage(new Image(localURL, false));
				regTextImage.setText(selectFile.getName());
			});
			//�ݱ� ��ư �������� ó���ϴ� �Լ�
			regBtncancle.setOnAction(e -> { registerStage.close(); });
	
			Scene scene= new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../application/register.css").toString());
			registerStage.setScene(scene);
			registerStage.show();
		} catch (IOException e) {}
		
	}
	//4. ���̺�� Ŭ�������� ó���ϴ� �Լ�
	private void handleB1TableViewAction(MouseEvent e) {
		//���̺�� ���þ����۰� �ε����� ����
		selectBook = b1TableView.getSelectionModel().getSelectedItem();
		selectBookIndex = b1TableView.getSelectionModel().getSelectedIndex();
		//�̹��� �̸����� å ���� �����´�.(ȯŸ����Ÿ.JPG => ȯŸ����Ÿ)
		firstImage= selectBook.getImage().split("\\.");
		if(e.getClickCount()==1) {
			b1BtnLook.setDisable(false);
			b1BtnStory.setDisable(false);
			b1BtnDrop.setDisable(false);
			b1BtnEdit.setDisable(false);
			b1ImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+selectBook.getImage()));
			// �ٰŸ� ��ư �������� ó��
			b1BtnStory.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						Stage storyStage = new Stage();
						storyStage.initModality(Modality.WINDOW_MODAL);
						storyStage.initOwner(mainStage);
						FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/story.fxml"));
						Parent root = loader.load();
						
						Label storyLabelTitle = (Label)root.lookup("#storyLabelTitle");
						TextArea storyTextArea = (TextArea)root.lookup("#storyTextArea");
						Button storyBtnReg = (Button)root.lookup("#storyBtnReg");
						ImageView storyImageView = (ImageView)root.lookup("#storyImageView");
						storyImageView.setImage(new Image(getClass().getResource("../images/story.jpg").toString()));
						storyStage.setTitle("Story");
						
						storyTextArea.setEditable(false);
						
						storyLabelTitle.setText(selectBook.getTitle());
						//�����ͺ��̽��� �ִ� �ٰŸ��� �ش� ��Ϲ�ȣ�� �̿��ؼ� �����´�.
						storyTextArea.setText(BookDAO.getBookStoryData(selectBook.getNo()));
						storyBtnReg.setOnAction(e -> storyStage.close() );
						
						Scene scene = new Scene(root);
						scene.getStylesheets().add(getClass().getResource("../application/story.css").toString());
						storyStage.setScene(scene);
						storyStage.show();
					}catch (IOException e1) { }
				}
			});
			// �б� ��ư �������� ó��
			b1BtnLook.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					try {
						Stage lookStage = new Stage();
						lookStage.initModality(Modality.WINDOW_MODAL);
						lookStage.initOwner(mainStage);
						FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/look.fxml"));
						Parent root = loader.load();
						
						TableView<SubBook> lookTableView= (TableView)root.lookup("#lookTableView");
						ImageView lookImageView = (ImageView)root.lookup("#lookImageView");
						Button lookBtnFirst = (Button)root.lookup("#lookBtnFirst");
						Button lookBtnAgain = (Button)root.lookup("#lookBtnAgain");
						Button lookBtnClose = (Button)root.lookup("#lookBtnClose");
						lookStage.setTitle(selectBook.getTitle());
						// ����â�� ���̺��� SubBook��Ŭ������ �����Ѵ�.
						TableColumn tcList= lookTableView.getColumns().get(0);
						tcList.setCellValueFactory(new PropertyValueFactory<>("list"));
						tcList.setStyle("-fx-alignment : CENTER;");
						
						TableColumn tcSubImage= lookTableView.getColumns().get(1);
						tcSubImage.setCellValueFactory(new PropertyValueFactory<>("subImage"));
						tcSubImage.setStyle("-fx-alignment : CENTER;");
						
						// �б�â�� ���̺�� ���� �����ϴ� list
						ObservableList<SubBook> lookList= FXCollections.observableArrayList();
						// �ϸ�â�� ���̺�� �̸��� �̹����� ������� �Է��ϴ� ����
						for(int i=0;i<selectBook.getNumber();i++) {
								lookList.add(new SubBook((1+i)+"",firstImage[0]+"_"+(1+i)+"_0.jpg" ));														
						}
						lookTableView.setItems(lookList);
						//�б�â���� ùȸ���� ��ư �������� ó���ϴ� �Լ�
						lookBtnFirst.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								count=0;
								try {
									Stage viewerStage = new Stage();
									viewerStage.initModality(Modality.WINDOW_MODAL);
									viewerStage.initOwner(lookStage);
									FXMLLoader viewerLoader= new FXMLLoader(getClass().getResource("../View/viewer.fxml"));
									Parent viewerRoot = viewerLoader.load();
									ImageView viewerImageView= (ImageView)viewerRoot.lookup("#viewerImageView");
									ImageView viewerImageBack = (ImageView)viewerRoot.lookup("#viewerImageBack");
									ImageView viewerImageNext = (ImageView)viewerRoot.lookup("#viewerImageNext");
									Label viewerLabelBack = (Label)viewerRoot.lookup("#viewerLabelBack");
									Label viewerLabelNext = (Label)viewerRoot.lookup("#viewerLabelNext");
									TextField viewerTextfield= (TextField)viewerRoot.lookup("#viewerTextfield");
									Button viewerBtnBack= (Button)viewerRoot.lookup("#viewerBtnBack");
									Button viewerBtnNext= (Button)viewerRoot.lookup("#viewerBtnNext");
									Button viewerBtnMark= (Button)viewerRoot.lookup("#viewerBtnMark");
									Button viewerBtnImage= (Button)viewerRoot.lookup("#viewerBtnImage");
									Button viewerBtnClose= (Button)viewerRoot.lookup("#viewerBtnClose");
									viewerImageNext.setImage(new Image(fileName+"������ȭ��ǥ.jpg"));
									viewerImageBack.setImage(new Image(fileName+"����ȭ��ǥ.jpg"));
									// ùȸ���⸦ Ŭ���ϸ� �׻� ù������ �����ֱ� ���� ����
									firstNumber = 1;
									viewerStage.setTitle(selectBook.getTitle()+" "+firstNumber+"��");
									// ���â���� ���� ������ ������ ���������� ���������� ���ڸ� �������ش�.
									viewerTextfield.setText(0+"");											
									viewerLabelBack.setText("");
									viewerLabelNext.setText(1+"");
									// ���â���� �ؽ�Ʈ�ʵ忡 ���� �Է��� ����ġ�� �ش� �������� �̵� (���� �������ϰ�� �˸��� ���� �� �������� ���ư���.)
									viewerTextfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
										@Override
										public void handle(KeyEvent event) {
											if(event.getCode().equals(KeyCode.ENTER)){
												int textCount=Integer.parseInt(viewerTextfield.getText());	// �ؽ�Ʈ�ʵ� ���� int�� �����Ѵ�.													
												try {
													viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+textCount+".jpg"));												
													viewerImageView.imageProperty().get().impl_getPlatformImage().toString(); // �ش� �������� ������ ������ ������ �ְ� ������ ���ܰ� �߻��Ѵ�.
													viewerTextfield.setText(textCount+"");
													viewerLabelNext.setText((textCount+1)+"");														
													if(!(textCount==0)) {
														viewerLabelBack.setText((textCount-1)+"");
													}else {
														viewerLabelBack.setText("");
													}
												}catch(Exception e) {													
													callAlert("���� : ���� �������Դϴ�."); // ���ܰ� �߻������� ���̹����� �����ְ� �����Ѵ�. 
													viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg"));
													viewerTextfield.setText(count+"");													
													viewerLabelNext.setText((count+1)+"");
													if(!viewerTextfield.getText().equals("0")) {
														viewerLabelBack.setText((count-1)+"");												
													}else {
														viewerLabelBack.setText("");											
													}
													return;
												}
												count=textCount; // �ش��̹����� �� ǥ�õǸ� �ش� ���������� ī��Ʈ�� �������ش�.
											}
										}
									});
									//���â ���� ù���� �������� 1�� 0�������� ����.
									viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_0.jpg"));
									// ������ ȭ��ǥ Ŭ���Ҷ����� count�� �������� �̹����� �ϳ��� ��ü���ش�.
									viewerImageNext.setOnMouseClicked(e1 -> {
										count++;
										viewerTextfield.setText(count+"");
										viewerLabelBack.setText((count-1)+"");
										viewerLabelNext.setText((count+1)+"");
										viewerImageNext.setVisible(true);
										try {
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg"));
											System.out.println(viewerImageView.imageProperty().get().impl_getPlatformImage().toString());										
										}catch(Exception e) {
											callAlert("������ ������ �Դϴ�. : ����������  �̵��ϼ���");
											count--;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg"));
											viewerTextfield.setText(count+"");
											viewerLabelNext.setText((count+1)+"");
											viewerLabelBack.setText((count-1)+"");
											return;
										}
									});
									// ���� ȭ��ǥ Ŭ���Ҷ����� count�� ���ҽ��� �̹����� �ϳ��� ��ü���ش�.
									viewerImageBack.setOnMouseClicked(e1 -> {
										count--;
										viewerTextfield.setText(count+"");											
										viewerLabelNext.setText((count+1)+"");
										if(!viewerTextfield.getText().equals("0")) {
											viewerLabelBack.setText((count-1)+"");												
										}else {
											viewerLabelBack.setText("");												
										}
										try {
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg"));	
											System.out.println(viewerImageView.imageProperty().get().impl_getPlatformImage().toString());										
										}catch(Exception e) {
											callAlert("ù������ �Դϴ�. : ����������  �̵��ϼ���");
											count++;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg"));
											viewerTextfield.setText(count+"");											
											viewerLabelNext.setText((count+1)+"");
											if(!viewerTextfield.getText().equals("0")) {
												viewerLabelBack.setText((count-1)+"");												
											}else {
												viewerLabelBack.setText("");												
											}
											return;
										}									
									});				
										// å���ǹ�ư�� ������ ó���ϴ� �Լ�
									viewerBtnMark.setOnAction(new EventHandler<ActionEvent>() {
										
										@Override
										public void handle(ActionEvent event) {
											bookDB=new BookDB(selectBook.getNo(), selectBook.getTitle(), 
													selectBook.getWriter(), selectBook.getScore(),
													selectBook.getGenre(), selectBook.getMdate(), selectBook.getState(),
													selectBook.getFinish(), selectBook.getNumber(), selectBook.getImage(),
													null,firstNumber+"/"+count+"");
											//å���������ư ������ �����ͺ��̽� markImage���̺� ���� �̹����� �ٽ� ������ �� �ֵ��� ������ �����Ѵ�( ȯŸ����Ÿ1��28������ => 1/28  )
											int count1 = BookDAO.updateMarkImageData(bookDB);
											if(count1!=0) {			
												callAlert("�ϸ�ũ ���� : "+selectBook.getTitle()+" "+firstNumber+"�� "+count+"���� �ϸ�ũ �Ǿ����ϴ�.");
											}else {
												return;
											}
										}
									});
									// ����ȭ ��ư �������� ó���ϴ� �Լ�
									viewerBtnBack.setOnAction(new EventHandler<ActionEvent>() {
										// �������� ���ҽ�Ű�� ù���������� ǥ���ϱ� ���� count�� 0���� ������ش�.
										@Override
										public void handle(ActionEvent event) {
											firstNumber--;
											if(firstNumber==0) {
												firstNumber++;
												callAlert("ù��°���Դϴ� : ùȸ�Դϴ�.");
												return;
											}
											viewerStage.setTitle(selectBook.getTitle()+" "+firstNumber+"��");
											viewerTextfield.setText("0");
											viewerLabelNext.setText("1");
											count=0;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg"));		
										}
									});
									// ����ȭ ��ư �������� ó���ϴ� �Լ�
									viewerBtnNext.setOnAction(new EventHandler<ActionEvent>() {
										// �������� ������Ű�� ù���������� ǥ���ϱ� ���� count�� 0���� ������ش�.
										@Override
										public void handle(ActionEvent event) {
											firstNumber++;
											if(firstNumber>selectBook.getNumber()) {
												firstNumber--;
												callAlert("���������Դϴ� : ������ȸ�Դϴ�.");
												return;
											}
											viewerStage.setTitle(selectBook.getTitle()+" "+firstNumber+"��");
											viewerTextfield.setText("0");
											viewerLabelNext.setText("1");
											count=0;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg"));		
										}
									});
									//�̹��������ư �������� ó���ϴ� �Լ�
									viewerBtnImage.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
												if(!imageDir.exists()) {
													imageDir.mkdir();	// ���丮�� ������ �ȵǾ� ������ ������ �����.
												}
												FileInputStream fis=null;
												BufferedInputStream bis=null;
												FileOutputStream fos=null;
												BufferedOutputStream bos=null;
												//���õ� �̹����� c:/images/"���õ��̹����̸���"���� �����Ѵ�.
												try {
													fis= new FileInputStream(imagePath+firstImage[0]+"/"+firstImage[0]+firstNumber+"/"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg");												
													bis= new BufferedInputStream(fis);													
													fos= new FileOutputStream(imageDir.getAbsolutePath()+"\\"+"book"+System.currentTimeMillis()+"_"+firstImage[0]+"_"+firstNumber+"_"+count+".jpg");
													bos= new BufferedOutputStream(fos);														
													int data=-1;
													while((data = bis.read()) !=- 1) {
														bos.write(data);
														bos.flush();
													}
													callAlert("�̹������� ���� : "+selectBook.getTitle()+" "+firstNumber+"�� "+count+"�� �̹����� C/comic ��ġ�� ���� �Ǿ����ϴ�.");
												} catch (Exception e) { 
													callAlert("�̹������� ���� : c/comic/�������� ���� ���˹ٶ�");
												} finally {
													try {
														if(fis != null)  { fis.close(); }
														if(bis != null)  { bis.close(); }
														if(fos != null)  { fos.close(); }
														if(bos != null)  { bos.close(); }
													} catch (IOException e) { }
												}// end of finally
										}
									});
									viewerBtnClose.setOnAction(e1 -> { viewerStage.close(); });
									
								Scene scene = new Scene(viewerRoot);
								scene.getStylesheets().add(getClass().getResource("../application/viewer.css").toString());
								viewerStage.setScene(scene);
								viewerStage.show();
							} catch (IOException e1) { }
							}
						});	
						//����â���� �̾�� Ŭ�������� ó���ϴ� �Լ�
						lookBtnAgain.setOnAction(new EventHandler<ActionEvent>() {
							
							@Override
							public void handle(ActionEvent event) {
								try {
									Stage viewerStage = new Stage();
									viewerStage.initModality(Modality.WINDOW_MODAL);
									viewerStage.initOwner(lookStage);
									FXMLLoader viewerLoader= new FXMLLoader(getClass().getResource("../View/viewer.fxml"));
									Parent viewerRoot = viewerLoader.load();
									ImageView viewerImageView= (ImageView)viewerRoot.lookup("#viewerImageView");
									ImageView viewerImageBack = (ImageView)viewerRoot.lookup("#viewerImageBack");
									ImageView viewerImageNext = (ImageView)viewerRoot.lookup("#viewerImageNext");
									Label viewerLabelBack = (Label)viewerRoot.lookup("#viewerLabelBack");
									Label viewerLabelNext = (Label)viewerRoot.lookup("#viewerLabelNext");
									TextField viewerTextfield= (TextField)viewerRoot.lookup("#viewerTextfield");
									Button viewerBtnBack= (Button)viewerRoot.lookup("#viewerBtnBack");
									Button viewerBtnNext= (Button)viewerRoot.lookup("#viewerBtnNext");
									Button viewerBtnMark= (Button)viewerRoot.lookup("#viewerBtnMark");
									Button viewerBtnImage= (Button)viewerRoot.lookup("#viewerBtnImage");
									Button viewerBtnClose= (Button)viewerRoot.lookup("#viewerBtnClose");
									viewerImageNext.setImage(new Image(fileName+"������ȭ��ǥ.jpg"));
									viewerImageBack.setImage(new Image(fileName+"����ȭ��ǥ.jpg"));
									// �����ͺ��̽����� �ش� ��Ϲ�ȣ�� markImage�� �����´� (4/25) 
									String markString= BookDAO.getBookMarkImageData(selectBook.getNo());
									if(markString==null) {
										callAlert("å���� ���� : ����� �������� �����ϴ�.");
										return;
									}		
									//�������ڿ� ���������ڸ� �����ش�(4/25 => 4   25)
									String[] markImage=markString.split("/");
									//�������� �ѱ� �� �ְ� �������� �ٲ��ش�.
									countPage = Integer.parseInt(markImage[1]);
									countFolder = Integer.parseInt(markImage[0]);
									viewerStage.setTitle(selectBook.getTitle()+" "+countFolder+"��");
									viewerTextfield.setText(countPage+"");
									viewerLabelBack.setText((countPage-1)+"");
									viewerLabelNext.setText((countPage+1)+"");
									// ���â���� �ؽ�Ʈ�ʵ忡 ���� �Է��� ����ġ�� �ش� �������� �̵� (���� �������ϰ�� �˸��� ���� �� �������� ���ư���.)
									viewerTextfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
										@Override
										public void handle(KeyEvent event) {
											if(event.getCode().equals(KeyCode.ENTER)){
												int textCount=Integer.parseInt(viewerTextfield.getText());	// �ؽ�Ʈ�ʵ� ���� int�� �����Ѵ�.													
												try { 
													viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+textCount+".jpg"));											
													viewerImageView.imageProperty().get().impl_getPlatformImage().toString();   // �ش� �������� ������ ������ ������ �ְ� ������ ���ܰ� �߻��Ѵ�.
													viewerTextfield.setText(textCount+"");
													viewerLabelNext.setText((textCount+1)+"");
													if(!(textCount==0)) {
														viewerLabelBack.setText((textCount-1)+"");
													}else {
														viewerLabelBack.setText("");
													}
												}catch(Exception e) {  // ���ܰ� �߻������� ���̹����� �����ְ� �����Ѵ�.
													callAlert("���������� : ���� ������ �Դϴ�.");
													viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));
													viewerTextfield.setText(countPage+"");													
													viewerLabelNext.setText((countPage+1)+"");
													if(!viewerTextfield.getText().equals("0")) {
														viewerLabelBack.setText((countPage-1)+"");												
													}else {
														viewerLabelBack.setText("");											
													}
													return;
												}
												countPage=textCount; // �������� ������ �ִ� ��� ī��Ʈ�� �Է��� ���� �����Ѵ�.
											}
										}
									});
									viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));	
									// ������ ȭ��ǥ Ŭ���Ҷ����� countPage�� �������� �̹����� �ϳ��� ��ü���ش�.
									viewerImageNext.setOnMouseClicked(e1 -> {
										countPage++;
										viewerTextfield.setText(countPage+"");
										viewerLabelBack.setText((countPage-1)+"");
										viewerLabelNext.setText((countPage+1)+"");
										try {
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));												
											viewerImageView.imageProperty().get().impl_getPlatformImage().toString();
										}catch(Exception e){
											callAlert("������ ������ �Դϴ�. : ����������  �̵��ϼ���");
											countPage--;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));
											viewerTextfield.setText(countPage+"");
											viewerLabelBack.setText((countPage-1)+"");
											viewerLabelNext.setText((countPage+1)+"");
											return;
										}
									});
									// ���� ȭ��ǥ Ŭ���Ҷ����� countPage�� ���ҽ��� �̹����� �ϳ��� ��ü���ش�.
									viewerImageBack.setOnMouseClicked(e1 -> {
										countPage--;
										viewerTextfield.setText(countPage+"");										
										viewerLabelNext.setText((countPage+1)+"");
										if(!viewerTextfield.getText().equals("0")) {
											viewerLabelBack.setText((countPage-1)+"");												
										}else {
											viewerLabelBack.setText("");												
										}
										try {
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));																								
											viewerImageView.imageProperty().get().impl_getPlatformImage().toString();
										}catch(Exception e){
											callAlert("ù ������ : ù ������ �Դϴ�.");
											countPage++;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));	
											viewerTextfield.setText(countPage+"");											
											viewerLabelNext.setText((countPage+1)+"");
											if(!viewerTextfield.getText().equals("0")) {
												viewerLabelBack.setText((countPage-1)+"");												
											}else {
												viewerLabelBack.setText("");												
											}
											return;	
										}
									});
									// ���â���� å���� ��ư Ŭ�������� ó���ϴ� �Լ�
									viewerBtnMark.setOnAction(new EventHandler<ActionEvent>() {
										
										@Override
										public void handle(ActionEvent event) {
											bookDB=new BookDB(selectBook.getNo(), selectBook.getTitle(), 
													selectBook.getWriter(), selectBook.getScore(),
													selectBook.getGenre(), selectBook.getMdate(), selectBook.getState(),
													selectBook.getFinish(), selectBook.getNumber(), selectBook.getImage(),
													null,countFolder+"/"+countPage);
											//å���������ư ������ �����ͺ��̽� markImage���̺� ���� �̹����� �ٽ� ������ �� �ֵ��� ������ �����Ѵ�( ȯŸ����Ÿ1��28������ => 1/28  )
											int count1 = BookDAO.updateMarkImageData(bookDB);
											if(count1!=0) {			
												callAlert("�ϸ�ũ ���� : "+selectBook.getTitle()+" "+countFolder+"�� "+countPage+"���� �ϸ�ũ �Ǿ����ϴ�.");
											}else {
												return;
											}
										}
									});
									// ����ȭ ��ư �������� ó���ϴ� �Լ�
									viewerBtnBack.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											countFolder--;
											if(countFolder==0) {
												countFolder++;
												callAlert("ù��°���Դϴ� : ùȸ�Դϴ�.");
												return;
											}
											viewerStage.setTitle(selectBook.getTitle()+" "+countFolder+"��");
											viewerTextfield.setText("0");
											viewerLabelNext.setText("1");
											countPage=0;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));		
										}
									});
									// ����ȭ ��ư �������� ó���ϴ� �Լ�
									viewerBtnNext.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											countFolder++;
											if(countFolder>selectBook.getNumber()) {
												countFolder--;
												callAlert("���������Դϴ� : ������ȸ�Դϴ�.");
												return;
											}
											viewerStage.setTitle(selectBook.getTitle()+" "+countFolder+"��");
											viewerTextfield.setText("0");
											viewerLabelNext.setText("1");
											countPage=0;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg"));		
										}
									});
									//�̹��������ư �������� ó���ϴ� �Լ�
									viewerBtnImage.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
												if(!imageDir.exists()) {
													imageDir.mkdir();	// ���丮�� ������ �ȵǾ� ������ ������ �����.
												}
												FileInputStream fis=null;
												BufferedInputStream bis=null;
												FileOutputStream fos=null;
												BufferedOutputStream bos=null;
												//���õ� �̹����� c:/images/"���õ��̹����̸���"���� �����Ѵ�.
												try {
													fis= new FileInputStream(imagePath+firstImage[0]+"/"+firstImage[0]+countFolder+"/"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg");
													bis= new BufferedInputStream(fis);
													fos= new FileOutputStream(imageDir.getAbsolutePath()+"\\"+"book"+System.currentTimeMillis()+"_"+firstImage[0]+"_"+countFolder+"_"+countPage+".jpg");
													bos= new BufferedOutputStream(fos);
													int data=-1;
													while((data = bis.read()) !=- 1) {
														bos.write(data);
														bos.flush();
													}
													callAlert("�̹������� ���� : "+selectBook.getTitle()+" "+countFolder+"�� "+countPage+"�� �̹����� C/comic ��ġ�� ���� �Ǿ����ϴ�.");
												} catch (Exception e) { 
													callAlert("�̹������� ���� : c/comic/�������� ���� ���˹ٶ�");
												} finally {
													try {
														if(fis != null)  { fis.close(); }
														if(bis != null)  { bis.close(); }
														if(fos != null)  { fos.close(); }
														if(bos != null)  { bos.close(); }
													} catch (IOException e) { }
												}// end of finally
											}
									});
									viewerBtnClose.setOnAction(e1 -> { viewerStage.close(); });
									
									Scene scene = new Scene(viewerRoot);
									scene.getStylesheets().add(getClass().getResource("../application/viewer.css").toString());
									viewerStage.setScene(scene);
									viewerStage.show();
								} catch (IOException e1) { }
							}
						});		
						
						// ����â�� ���̺�� ����(1��Ŭ��=�̹����� ����, 2��Ŭ��=viewerâ�� ���)
						lookTableView.setOnMouseClicked( e ->{
							//����â�� ���̺�� ���þ������� ����
							selectSubBook= lookTableView.getSelectionModel().getSelectedItem();
							//���̺�� ��������� int�� �ٲ۴�.
							folderNum=Integer.parseInt(selectSubBook.getList());
							//Ŭ���϶����� count�� 0���� ���� ���â ��ﶧ���� 0���������� �����ش�.
							count=0;
							lookImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+selectSubBook.getSubImage()));
							if(e.getClickCount()==2) {
								try {
									Stage viewerStage = new Stage();
									viewerStage.initModality(Modality.WINDOW_MODAL);
									viewerStage.initOwner(lookStage);
									FXMLLoader viewerLoader= new FXMLLoader(getClass().getResource("../View/viewer.fxml"));
									Parent viewerRoot = viewerLoader.load();
									ImageView viewerImageView= (ImageView)viewerRoot.lookup("#viewerImageView");
									ImageView viewerImageBack = (ImageView)viewerRoot.lookup("#viewerImageBack");
									ImageView viewerImageNext = (ImageView)viewerRoot.lookup("#viewerImageNext");
									Label viewerLabelBack = (Label)viewerRoot.lookup("#viewerLabelBack");
									Label viewerLabelNext = (Label)viewerRoot.lookup("#viewerLabelNext");
									TextField viewerTextfield= (TextField)viewerRoot.lookup("#viewerTextfield");
									Button viewerBtnBack= (Button)viewerRoot.lookup("#viewerBtnBack");
									Button viewerBtnNext= (Button)viewerRoot.lookup("#viewerBtnNext");
									Button viewerBtnMark= (Button)viewerRoot.lookup("#viewerBtnMark");
									Button viewerBtnImage= (Button)viewerRoot.lookup("#viewerBtnImage");
									Button viewerBtnClose= (Button)viewerRoot.lookup("#viewerBtnClose");
									viewerStage.setTitle(selectBook.getTitle()+" "+folderNum+"��");
									viewerImageNext.setImage(new Image(fileName+"������ȭ��ǥ.jpg"));
									viewerImageBack.setImage(new Image(fileName+"����ȭ��ǥ.jpg"));
									viewerLabelBack.setText("");
									viewerLabelNext.setText((count+1)+"");
									viewerTextfield.setText(count+"");
									
									// ���â���� �ؽ�Ʈ�ʵ忡 ���� �Է��� ����ġ�� �ش� �������� �̵� (���� �������ϰ�� �˸��� ���� �� �������� ���ư���.)
									viewerTextfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
										@Override
										public void handle(KeyEvent event) {
											if(event.getCode().equals(KeyCode.ENTER)){
												int textCount=Integer.parseInt(viewerTextfield.getText());	// �ؽ�Ʈ�ʵ� ���� int�� �����Ѵ�.													
												try {  
													viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+textCount+".jpg"));
													viewerImageView.imageProperty().get().impl_getPlatformImage().toString();  // �ش� �������� ������ ������ ������ �ְ� ������ ���ܰ� �߻��Ѵ�.
													viewerTextfield.setText(textCount+"");
													viewerLabelNext.setText((textCount+1)+"");
													if(!(textCount==0)) {
														viewerLabelBack.setText((textCount-1)+"");
													}else {
														viewerLabelBack.setText("");
													}
												}catch(Exception e){  // ���ܰ� �߻������� ���̹����� �����ְ� �����Ѵ�.
													callAlert("���������� : ���� ������ �Դϴ�.");
													viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg"));
													viewerTextfield.setText(count+"");													
													viewerLabelNext.setText((count+1)+"");
													if(!viewerTextfield.getText().equals("0")) {
														viewerLabelBack.setText((count-1)+"");												
													}else {
														viewerLabelBack.setText("");											
													}
													return;
												}
												count=textCount; // �������� ������ �ִ� ��� ī��Ʈ�� �Է��� ���� �����Ѵ�.
											}
										}
									});
									//���â ���� ù���� �������� 0�������� ����.
									viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+selectSubBook.getSubImage()));	
									// ������ ȭ��ǥ Ŭ���Ҷ����� count�� �������� �̹����� �ϳ��� ��ü���ش�.
									viewerImageNext.setOnMouseClicked(e1 ->  {  
										count++;
										viewerTextfield.setText(count+"");
										viewerLabelBack.setText((count-1)+"");
										viewerLabelNext.setText((count+1)+"");
										try{
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg"));
											viewerImageView.imageProperty().get().impl_getPlatformImage().toString();																								
										}catch(Exception e2) {
											callAlert("������ ������ �Դϴ�. : ����������  �̵��ϼ���");
											count--;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg"));
											viewerTextfield.setText(count+"");
											viewerLabelBack.setText((count-1)+"");
											viewerLabelNext.setText((count+1)+"");
											return;											
										}										
									});
									// ���� ȭ��ǥ Ŭ���Ҷ����� count�� ���ҽ��� �̹����� �ϳ��� ��ü���ش�.
									viewerImageBack.setOnMouseClicked(e1 -> {
										count--;
										viewerTextfield.setText(count+"");
										viewerLabelNext.setText((count+1)+"");										
										if(!viewerTextfield.getText().equals("0")) {
											viewerLabelBack.setText((count-1)+"");												
										}else {
											viewerLabelBack.setText("");											
										}
										try {
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg"));
											viewerImageView.imageProperty().get().impl_getPlatformImage().toString();																								
										}catch(Exception e2) {
											callAlert("ù ������ : ù ������ �Դϴ�.");
											count++;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg"));
											viewerTextfield.setText(count+"");											
											viewerLabelNext.setText((count+1)+"");
											if(!viewerTextfield.getText().equals("0")) {
												viewerLabelBack.setText((count-1)+"");												
											}else {
												viewerLabelBack.setText("");												
											}
											return;											
										}									
									});
									// ����ȭ ��ư �������� ó���ϴ� �Լ�
									viewerBtnBack.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											folderNum--;
											if(folderNum==0) {
												folderNum++;
												callAlert("ù��°���Դϴ� : ùȸ�Դϴ�.");
												return;
											}
											viewerStage.setTitle(selectBook.getTitle()+" "+folderNum+"��");
											viewerTextfield.setText("0");
											viewerLabelNext.setText("1");
											count=0;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg"));		
										}
									});
									// ����ȭ ��ư �������� ó���ϴ� �Լ�
									viewerBtnNext.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
											folderNum++;
											if(folderNum>selectBook.getNumber()) {
												folderNum--;
												callAlert("���������Դϴ� : ������ȸ�Դϴ�.");
												return;
											}
											viewerStage.setTitle(selectBook.getTitle()+" "+folderNum+"��");
											viewerTextfield.setText("0");
											viewerLabelNext.setText("1");
											count=0;
											viewerImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg"));		
										}
									});
									
									// å���������ư�� ������ ó���ϴ� �Լ�
									viewerBtnMark.setOnAction(new EventHandler<ActionEvent>() {
										
										@Override
										public void handle(ActionEvent event) {
											bookDB=new BookDB(selectBook.getNo(), selectBook.getTitle(), 
													selectBook.getWriter(), selectBook.getScore(),
													selectBook.getGenre(), selectBook.getMdate(), selectBook.getState(),
													selectBook.getFinish(), selectBook.getNumber(), selectBook.getImage(),
													null,folderNum+"/"+count);
											//å���������ư ������ �����ͺ��̽� markImage���̺� ���� �̹����� �ٽ� ������ �� �ֵ��� ������ �����Ѵ�( ȯŸ����Ÿ1��28������ => 1/28  )
											int count1 = BookDAO.updateMarkImageData(bookDB);
											if(count1!=0) {			
												callAlert("�ϸ�ũ ���� : "+selectBook.getTitle()+" "+folderNum+"�� "+count+"���� �ϸ�ũ �Ǿ����ϴ�.");
											}else {
												return;
											}
										}
									});				
									//�̹��������ư �������� ó���ϴ� �Լ�
									viewerBtnImage.setOnAction(new EventHandler<ActionEvent>() {
										@Override
										public void handle(ActionEvent event) {
												if(!imageDir.exists()) {
													imageDir.mkdir();	// ���丮�� ������ �ȵǾ� ������ ������ �����.
												}
												FileInputStream fis=null;
												BufferedInputStream bis=null;
												FileOutputStream fos=null;
												BufferedOutputStream bos=null;
												//���õ� �̹����� c:/images/"���õ��̹����̸���"���� �����Ѵ�.
												try {
													fis= new FileInputStream(imagePath+firstImage[0]+"/"+firstImage[0]+folderNum+"/"+firstImage[0]+"_"+folderNum+"_"+count+".jpg");
													bis= new BufferedInputStream(fis);
													fos= new FileOutputStream(imageDir.getAbsolutePath()+"\\"+"book"+System.currentTimeMillis()+"_"+firstImage[0]+"_"+folderNum+"_"+count+".jpg");
													bos= new BufferedOutputStream(fos);
													int data=-1;
													while((data = bis.read()) !=- 1) {
														bos.write(data);
														bos.flush();
													}
													callAlert("�̹������� ���� : "+selectBook.getTitle()+" "+folderNum+"�� "+count+"�� �̹����� C/comic ��ġ�� ���� �Ǿ����ϴ�.");
												} catch (Exception e) { 
													callAlert("�̹������� ���� : c/comic/�������� ���� ���˹ٶ�");
												} finally {
													try {
														if(fis != null)  { fis.close(); }
														if(bis != null)  { bis.close(); }
														if(fos != null)  { fos.close(); }
														if(bos != null)  { bos.close(); }
													} catch (IOException e) { }
												}// end of finally
										}
									});
									viewerBtnClose.setOnAction(e1 -> { viewerStage.close(); });
									
									Scene scene = new Scene(viewerRoot);
									scene.getStylesheets().add(getClass().getResource("../application/viewer.css").toString());
									viewerStage.setScene(scene);
									viewerStage.show();
								} catch (IOException e1) { }
							}
						});
						lookBtnClose.setOnAction(e -> { lookStage.close(); });
						
						Scene scene = new Scene(root);
						scene.getStylesheets().add(getClass().getResource("../application/look.css").toString());
						lookStage.setScene(scene);
						lookStage.show();
					}catch (IOException e1) { }
				}
			});
		}	
	}
	//5. ������ư �������� ó���ϴ� �Լ�
	private void handleBtnDropAction() {
		//�ش� ��Ϲ�ȣ�� �̿��� db�� ���̺�信�� �ش� ���� ����
		int count = BookDAO.deleteBookData(selectBook.getNo());
		if(count!=0) {
			b1ListDate.remove(selectBookIndex);
			dbArrayList.remove(selectBook);
			callAlert("�����Ϸ� : "+selectBook.getTitle()+"å�� �����Ǿ����ϴ�.");			
		}else {
			return;
		}	
	}
	//6. ������ư �������� ó���ϴ� �Լ�
	private void handleBtnEditAction() {
		try {
			Stage editStage = new Stage();
			editStage.initModality(Modality.WINDOW_MODAL);
			editStage.initOwner(mainStage);
			FXMLLoader loader= new FXMLLoader(getClass().getResource("../View/register.fxml"));		
			Parent root = loader.load();
			
			TextField regTextNo = (TextField)root.lookup("#regTextNo");
			TextField regTextWriter = (TextField)root.lookup("#regTextWriter");
			TextField regTextImage = (TextField)root.lookup("#regTextImage");
			TextField regTextState = (TextField)root.lookup("#regTextState");
			TextField regTextTitle = (TextField)root.lookup("#regTextTitle");
			TextField regTextScore = (TextField)root.lookup("#regTextScore");
			TextField regTextNumber = (TextField)root.lookup("#regTextNumber");
			TextArea regTextStory = (TextArea)root.lookup("#regTextStory");
			ComboBox regCmbGenre = (ComboBox)root.lookup("#regCmbGenre");
			ComboBox regCmbFinish = (ComboBox)root.lookup("#regCmbFinish");
			DatePicker  regDatePickerDate = (DatePicker)root.lookup("#regDatePickerDate");
			ImageView regImageView = (ImageView)root.lookup("#regImageView");
			Button regBtnImage = (Button)root.lookup("#regBtnImage");
			Button regBtnSave = (Button)root.lookup("#regBtnSave");
			Button regBtncancle = (Button)root.lookup("#regBtncancle");
			editStage.setTitle("Edit");
			//����â���� �Ǽ�, �ϰῩ��, ������ �Է°����ϰ� �������� ���� ���ϵ��� ����
			regTextNo.setEditable(false);
			regTextWriter.setEditable(false);
			regTextImage.setEditable(false);
			regTextState.setEditable(false);
			regTextTitle.setEditable(false);
			regTextStory.setEditable(false);
			regDatePickerDate.setEditable(true);
			regCmbGenre.setEditable(false);
			regBtnImage.setVisible(false);
			//�޺��ڽ� ���� ����
			regCmbFinish.setItems(regCmbFinishList);
			
			regTextNo.setText(selectBook.getNo()+"");
			regTextWriter.setText(selectBook.getWriter());
			regTextImage.setText(selectBook.getImage());
			regTextState.setText(selectBook.getState());
			regTextTitle.setText(selectBook.getTitle());
			regTextScore.setText(selectBook.getScore()+"");
			regTextNumber.setText(selectBook.getNumber()+"");
			regCmbGenre.setValue(selectBook.getGenre());
			regCmbFinish.setValue(selectBook.getFinish());
			regDatePickerDate.setValue(selectBook.getMdate().toLocalDate());
			regImageView.setImage(new Image(fileName+"comic/"+firstImage[0]+"/"+selectBook.getImage()));
			//�ٰŸ��� �����ͺ��̽� ���̺��� �����´�.
			regTextStory.setText(BookDAO.getBookStoryData(selectBook.getNo())); 
			//�����ư �������� ���̺���� ���� ����.
			regBtnSave.setOnAction(e -> {
				try {
					// ������ ���� ���̺��� �����ͺ��̽� ���̺��� �����Ų��.
					book=new Book(Integer.parseInt(regTextNo.getText()), regTextTitle.getText(), 
							regTextWriter.getText(), Double.parseDouble(regTextScore.getText()),
							regCmbGenre.getValue().toString(), Date.valueOf(regDatePickerDate.getValue().toString()), regTextState.getText(),
							regCmbFinish.getValue().toString(), Integer.parseInt(regTextNumber.getText()), regTextImage.getText());
					
					bookDB=new BookDB(Integer.parseInt(regTextNo.getText()), regTextTitle.getText(), 
							regTextWriter.getText(), Double.parseDouble(regTextScore.getText()),
							regCmbGenre.getValue().toString(), Date.valueOf(regDatePickerDate.getValue().toString()), regTextState.getText(),
							regCmbFinish.getValue().toString(), Integer.parseInt(regTextNumber.getText()), regTextImage.getText(),
							regTextStory.getText(),null);
					callAlert("�����Ϸ� : "+selectBook.getTitle()+" å�� �����Ǿ����ϴ�.");
					editStage.close();
					//������ ���� �����ͺ��̽��� �����Ų��. => ���������� ���氡���ϰ� �س��� ���� �Ǽ�, ����, �ϰῩ���̴�.
					int count = BookDAO.updateBookData(bookDB);  
					if(count!=0) {
						b1ListDate.set(selectBookIndex, book);					// list���� ���� ���̺�� ���õ� ��ü�� �������ش�.
						int arrayIndex= dbArrayList.indexOf(selectBook);		// dbArrayList���� ������ ��ü�� ���� ���õ� ���̺�並 �̿��ؼ� �ε����� �����´�
						dbArrayList.set(arrayIndex, book);							// �ε����� �̿��ؼ� dbArrayList�� �������ش�.
					}else {
						return;
					}					
				}catch (NumberFormatException e1) {
					callAlert("���� ���� : ����, �Ǽ��� �Է��ϼ���.");
				} catch(Exception e1) { }
			});
			//�ݱ� ��ư �������� ó���ϴ� �Լ�
			regBtncancle.setOnAction(e -> { editStage.close(); });
			Scene scene= new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../application/edit.css").toString());
			editStage.setScene(scene);
			editStage.show();
		} catch(Exception e) { }
	}
	//7. �ݱ��ư �������� ó���ϴ� �Լ�
	private void handleBtnCloseAction() {
		Platform.exit();
	}
	//8. �˻���ư �������� ó���ϴ� �Լ�
	private void handleBtnSearchAction() {
		// �˻���� ��Ϲ�ȣ�� ������������ �˻��Ѵ�.
		for(Book book : b1ListDate ) {
			if(b1TextSearch.getText().trim().equals(book.getNo()+"") || b1TextSearch.getText().trim().equals(book.getTitle())) {
				b1TableView.getSelectionModel().select(book);
				b1TableView.scrollTo(book);
				b1ImageView.setImage(new Image(fileName+"comic/"+book.getTitle()+"/"+book.getImage()));
			}
		}
	}
 	//9. ��Ʈ��ư �������� ó���ϴ� �Լ�
	private void handleBtnChartAction() {
		try {
			Stage chartStage = new Stage();
			chartStage.initModality(Modality.WINDOW_MODAL);
			chartStage.initOwner(mainStage);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/chart.fxml"));
			Parent root = loader.load();
			
			BarChart t1BarChart = (BarChart)root.lookup("#t1BarChart");
			PieChart t2PieChart = (PieChart)root.lookup("#t2PieChart");
			Button t1BtnReg = (Button)root.lookup("#t1BtnReg");
			Button t2BtnReg = (Button)root.lookup("#t2BtnReg");
			chartStage.setTitle("Chart");
			//ù��° tab => ������ barChart�� �����ش�.
			XYChart.Series seriesScore = new  XYChart.Series<>();
			seriesScore.setName("����");
			ObservableList scoreList= FXCollections.observableArrayList();
			for(int i=0; i<b1ListDate.size(); i++) { 
				scoreList.add(new XYChart.Data<>(b1ListDate.get(i).getTitle(),b1ListDate.get(i).getScore()));
				
			}
			seriesScore.setData(scoreList);
			t1BarChart.getData().add(seriesScore);
			//�ι�° tab=> �帣�� count���� ������Ʈ�� �����ش�. => �������� �̿��ؼ� �����ͺ��̽����� �帣�� count���� �����´�.
			ObservableList GenreList= FXCollections.observableArrayList();
			GenreList.add(new PieChart.Data("������",  BookDAO.getBookGenreData("������")));
			GenreList.add(new PieChart.Data("�׼�",  BookDAO.getBookGenreData("�׼�")));
			GenreList.add(new PieChart.Data("�ҳ�",  BookDAO.getBookGenreData("�ҳ�")));
			GenreList.add(new PieChart.Data("������",  BookDAO.getBookGenreData("������")));
			GenreList.add(new PieChart.Data("��Ÿ��",  BookDAO.getBookGenreData("��Ÿ��")));
			GenreList.add(new PieChart.Data("���",  BookDAO.getBookGenreData("���")));
			GenreList.add(new PieChart.Data("���",  BookDAO.getBookGenreData("���")));
			GenreList.add(new PieChart.Data("�ڹ�",  BookDAO.getBookGenreData("�ڹ�")));
			
			t2PieChart.setData(GenreList);
			//�ݱ� ��ư �������� ó���ϴ� �Լ�
			t1BtnReg.setOnAction( e -> { chartStage.close(); });
			t2BtnReg.setOnAction( e -> { chartStage.close(); });
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../application/chart.css").toString());
			chartStage.setScene(scene);
			chartStage.show();
		} catch (IOException e) { }
	}
	//��Ÿ : �˸�â
	public static void callAlert(String contentText) {
		Alert alert= new Alert(AlertType.INFORMATION);
		alert.setTitle("�˸�â");
		alert.setHeaderText(contentText.substring(0, contentText.lastIndexOf(":")));
		alert.setContentText(contentText.substring(contentText.lastIndexOf(":")+1));
		alert.showAndWait();
	}
	//��Ÿ : �ش� �ؽ�Ʈ�ʵ忡 ���� �ڸ����� �����ִ� �Լ�
	private void inputDecimalFormat(TextField textField) {
		// ���ڸ� �Է�(������ �Է¹���)
		DecimalFormat format = new DecimalFormat("#####");
		// ���� �Է½� ���� ���� �̺�Ʈ ó��
		textField.setTextFormatter(new TextFormatter<>(event -> {  
			//�Է¹��� ������ ������ �̺�Ʈ�� ������.  
			if (event.getControlNewText().isEmpty()) { return event; }
			//������ �м��� ���� ��ġ�� ������. 
			ParsePosition parsePosition = new ParsePosition(0);
			//�Է¹��� ����� �м���ġ�� �������������� format ����� ��ġ���� �м���.
			Object object = format.parse(event.getControlNewText(), parsePosition); 
			//���ϰ��� null �̰ų�, �Է��ѱ��̿� �����м���ġ���� ���������(�� �м������������� ����) �ų�, �Է��ѱ��̰� 6�̸�(5�ڸ��� �Ѿ����� ����.) �̸� null ������. 
			if (object == null || parsePosition.getIndex()<event.getControlNewText().length() || event.getControlNewText().length() == 6) {
				return null;    
			}else {
				return event;    
			}   
		}));
	}
}