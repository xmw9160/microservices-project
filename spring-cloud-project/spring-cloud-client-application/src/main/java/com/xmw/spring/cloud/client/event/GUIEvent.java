package com.xmw.spring.cloud.client.event;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author xmw.
 * @date 2018/10/20 9:10.
 */
public class GUIEvent {
    public static void main(String[] args) {
        final JFrame frame = new JFrame("简单GUI程序-Java事件/监听机制");
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                System.out.printf("[%s]事件: %s\n", Thread.currentThread().getName(), event);
            }
        });

        frame.setBounds(300, 300, 400, 300);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("宽口正在关闭");
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("宽口已经关闭");
                System.exit(0);
            }
        });
    }
}
