fileName="file:///D:/"
imagePath="D:/comic/"
//�̹��� �̸��� å����_1_0 �� �������� �����Ͽ��� �Ѵ�.(2�� => å����_2_0)
//���� ���� -> å����(����) => å����1~å����20(����) => å����_0_0(�̹�������)
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