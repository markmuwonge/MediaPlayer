package com.markmuwonge.MediaPlayer;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class Player {
	
	JFrame frame;
	JPanel panel, panel2;
	JMenuBar menuBar;
	JMenu fileMenuCategory;
	JMenuItem openMenuOption;
	JButton backTenSecsBtn, playOrPauseBtn, stopBtn, forwardTenSecsBtn;
	
	MediaPlayerFactory mediaPlayerFactory;
	EmbeddedMediaPlayer embeddedMediaPlayer;
	
	JFileChooser chooser;
	String choosertitle;
	Canvas canvas;
	
	ArrayList <JButton> buttonControls = new ArrayList<>();
	
	
	public Player()
	{
		frame = new JFrame();
		frame.setTitle("Mark Muwonge's Media Player");
		frame.setSize(1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		menuBar = new JMenuBar();
		fileMenuCategory = new JMenu("File");
		openMenuOption = new JMenuItem("Open");
		openMenuOption.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				selectFile();
			}
		});
		
		fileMenuCategory.add(openMenuOption);
		menuBar.add(fileMenuCategory);
		frame.setJMenuBar(menuBar);
		
		canvas = new Canvas();
		canvas.setBackground(Color.black);
		canvas.setSize(1000,450);
		
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		
		backTenSecsBtn = new JButton("-10s");
		backTenSecsBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				embeddedMediaPlayer.skip(-10000);
			}
		});
		playOrPauseBtn = new JButton("Play");
		playOrPauseBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (playOrPauseBtn.getText().equals("Play"))
				{
					playMedia();
				}
				else
				{
					pauseMedia();
				}
			}
		});
		stopBtn = new JButton("Stop");
		stopBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				embeddedMediaPlayer.stop();
				pauseMedia();
			}
		});
		forwardTenSecsBtn = new JButton("+10s");
		forwardTenSecsBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				embeddedMediaPlayer.skip(+10000);
			}
		});
		buttonControls.add(backTenSecsBtn);
		buttonControls.add(playOrPauseBtn);
		buttonControls.add(stopBtn);
		buttonControls.add(forwardTenSecsBtn);
		
		areControlButtonsVisible(false);
		
		panel2 = new JPanel();
		panel2.setLayout(new FlowLayout());
				
		panel.add(canvas);
		panel2.add(backTenSecsBtn);
		panel2.add(playOrPauseBtn);
		panel2.add(stopBtn);
		panel2.add(forwardTenSecsBtn);
		
		frame.add(panel, BorderLayout.NORTH);
		frame.add(panel2, BorderLayout.SOUTH);
			
		frame.setVisible(true);
		
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "lib");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		mediaPlayerFactory = new MediaPlayerFactory(); 
	}
	
	private void selectFile()
	{
        chooser = new JFileChooser();
        // optionally set chooser options ...
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) 
        {
        	areControlButtonsVisible(true);

            embeddedMediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(frame));
            embeddedMediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(canvas));        
            embeddedMediaPlayer.prepareMedia(chooser.getSelectedFile().getAbsolutePath());
            
            playMedia();
        } 
        else 
        {
        	
        }
        
	}
	
	private void areControlButtonsVisible(final boolean option)
	{
		buttonControls.stream().forEach(x -> x.setVisible(option));
	}
	
	private void playMedia()
	{
		embeddedMediaPlayer.play();
		playOrPauseBtn.setText("Pause");
	}
	
	private void pauseMedia()
	{
		embeddedMediaPlayer.pause();
		playOrPauseBtn.setText("Play");
	}
	
}
