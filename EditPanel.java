import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class EditPanel extends JPanel {
	private TextSource textSource = null;
	private JTextField tf = new JTextField(10);
	
	public EditPanel(TextSource textSource) {
		this.textSource = textSource;
		this.setBackground(Color.CYAN);
		this.add(tf);
		
		JButton editBtn = new JButton("단어 추가");
		this.add(editBtn);
		
		editBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String text = tf.getText();
				if(text.length() == 0) //입력된 텍스트가 공백이면 종료
					return;
				textSource.add(text); //TextSource에 단어 추가
				tf.setText(""); //텍스트 필드 지워주기
			}
			
		});
	}
}
