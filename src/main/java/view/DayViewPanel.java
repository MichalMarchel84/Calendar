package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDate;

class DayViewPanel extends JPanel implements LanguageListener{

    private static final int hoursOnDisplay = 12;
    private static final int bufferUpdate = 2;
    private final DayPanel content = new DayPanel();
    private final JScrollPane scroll = createScroll();

    DayViewPanel(){
        this.setLayout(new BorderLayout());
        this.add(scroll, BorderLayout.CENTER);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                content.setDate(LocalDate.now());
            }
        });
    }

    private JScrollPane createScroll(){
        JScrollPane scroll = new JScrollPane(content);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        scroll.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                resizeContent();
                SwingUtilities.invokeLater(() -> {
                    int sv = (scroll.getVerticalScrollBar().getMaximum() - scroll.getViewport().getHeight())/2;
                    scroll.getVerticalScrollBar().setValue(sv);
                    resizeContent();
                });
            }
        });

        scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if(e.getValue() == 0){
                    changeBufferUp();
                }
                else if(e.getValue() == (scroll.getVerticalScrollBar().getMaximum() - scroll.getViewport().getHeight())){
                    changeBufferDown();
                }
            }
        });

        return scroll;
    }

    private void changeBufferUp(){
        content.moveBuffer(-bufferUpdate);
        scroll.getVerticalScrollBar().setValue(scroll.getViewport().getHeight()*(24*bufferUpdate/hoursOnDisplay));
    }

    private void changeBufferDown(){
        content.moveBuffer(bufferUpdate);
        scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getValue() - scroll.getViewport().getHeight()*(24*bufferUpdate/hoursOnDisplay));
    }

    private void resizeContent(){
        int w = scroll.getViewport().getWidth();
        double h = scroll.getViewport().getHeight()/hoursOnDisplay;
        content.setPreferredSize(new Dimension(w, (int)(24*(2*DayPanel.daysInBuffer + 1)*h)));
    }

    @Override
    public void languageChanged() {
        content.repaint();
    }
}
