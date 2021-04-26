
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;

public class TypingTutor extends JFrame implements ActionListener, KeyListener {
	private JLabel lblTimer;
	private JLabel lblScore;
	private JLabel lblWord;
	private JTextField txtWord;
	private JButton btnStart;
	private JButton btnStop;

	private Timer clockTimer = null;
	private Timer wordTimer = null;

	private boolean running = false;
	private int timeRem = 0;
	private int score = 0;
	private String[] words = null;

	public TypingTutor(String[] args) {
		words = args;

		GridLayout layout = new GridLayout(3, 2);
		super.setLayout(layout);

		Font font = new Font("Comic Sans MS", 1, 80);

		lblTimer = new JLabel("Time");
		lblTimer.setFont(font);
		super.add(lblTimer);

		lblScore = new JLabel("Score");
		lblScore.setFont(font);
		super.add(lblScore);

		lblWord = new JLabel("");
		lblWord.setFont(font);
		super.add(lblWord);

		txtWord = new JTextField("");
		txtWord.setFont(font);
		txtWord.addKeyListener(this);
		super.add(txtWord);

		btnStart = new JButton("Start");
		btnStart.setFont(font);
		btnStart.addActionListener(this);
		super.add(btnStart);

		btnStop = new JButton("Stop");
		btnStop.setFont(font);
		btnStop.addActionListener(this);
		super.add(btnStop);

		super.setTitle("Typing Tutor");
		super.setExtendedState(MAXIMIZED_BOTH);
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		super.setVisible(true);

		setUPGame();
	}

	private void setUPGame() {
		clockTimer = new Timer(1000, this);
		clockTimer.setInitialDelay(0);

		wordTimer = new Timer(4000, this);
		wordTimer.setInitialDelay(0);

		running = false;
		timeRem = 60;
		score = 0;

		lblTimer.setText("Time: " + timeRem);
		lblScore.setText("Score: " + score);
		lblWord.setText("");
		txtWord.setText("");
		btnStart.setText("Start");
		btnStop.setText("Stop");

		txtWord.setEnabled(false);
		btnStop.setEnabled(false);
	}

	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnStart) {
			handleStart();
		} else if (e.getSource() == btnStop) {
			handleStop();
		} else if (e.getSource() == clockTimer) {
			handleClockTimer();
		} else if (e.getSource() == wordTimer) {
			handleWordTimer();
		}
	}

	private void handleStart() {
		if (running == false) {
			clockTimer.start();
			wordTimer.start();

			running = true;
			btnStart.setText("Pause");
			txtWord.setEnabled(true);
			btnStop.setEnabled(true);

			txtWord.setFocusCycleRoot(true);
			super.transferFocus();
		} else {
			clockTimer.stop();
			wordTimer.stop();

			running = false;
			btnStart.setText("Start");
			txtWord.setEnabled(false);
			btnStop.setEnabled(false);
		}
	}

	private void handleWordTimer() {
		String actual = lblWord.getText();
		String expected = txtWord.getText();
		if (expected.length() > 0 && actual.equals(expected))
			score++;

		lblScore.setText("Score: " + score);

		int ridx = (int) (Math.random() * words.length);
		lblWord.setText(words[ridx]);
		txtWord.setText("");
	}

	private void handleClockTimer() {
		timeRem--;
		lblTimer.setText("Time: " + timeRem);

		if (timeRem == 0)
			handleStop();
	}

	private void handleStop() {
		clockTimer.stop();
		wordTimer.stop();

		int choice = JOptionPane.showConfirmDialog(this, "Wish to continue?");

		if (choice == JOptionPane.YES_OPTION) {
			setUPGame();
		} else if (choice == JOptionPane.NO_OPTION) {
			JOptionPane.showMessageDialog(this, "Final Score: " + score);
			super.dispose();
		} else {
			if (timeRem == 0) {
				setUPGame();
			} else {
				clockTimer.start();
				wordTimer.start();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		String actual = lblWord.getText();
		String expected = txtWord.getText();
		if (expected.length() > 0 && actual.equals(expected)) {
			score++;

			lblTimer.setText("Time: " + timeRem);
			lblScore.setText("Score: " + score);

			int ridx = (int) (Math.random() * words.length);
			lblWord.setText(words[ridx]);
			txtWord.setText("");

			wordTimer.restart();
		}
	}

}