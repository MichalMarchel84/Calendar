package view;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDate;

public class DayViewPanel extends JPanel implements LanguageListener{

    private boolean inhibit = true;

    private static final int hoursOnDisplay = 12;
    private static final int bufferUpdate = 2;
    private final DayPanel content = new DayPanel();
    private final JScrollPane scroll = createScroll();

    public DayViewPanel(){

        I18n.addLanguageListener(this);

        this.setLayout(new BorderLayout());
        this.add(scroll, BorderLayout.CENTER);
        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                Thread t = new Thread(() -> {
                    try {
                        Thread.sleep(200);
                        int sv = (scroll.getVerticalScrollBar().getMaximum() - scroll.getViewport().getHeight())/2;
                        scroll.getVerticalScrollBar().setValue(sv);
                        inhibit = false;
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                });
                t.start();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                inhibit = true;
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {

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
                int sv = (scroll.getVerticalScrollBar().getMaximum() - scroll.getViewport().getHeight())/2;
                scroll.getVerticalScrollBar().setValue(sv);
            }
        });

        scroll.getVerticalScrollBar().addAdjustmentListener(e -> {
            if(!inhibit) {
                if (e.getValue() == 0) {
                    changeBufferUp();
                } else if (e.getValue() == (scroll.getVerticalScrollBar().getMaximum() - scroll.getViewport().getHeight())) {
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
        double h = (double) scroll.getViewport().getHeight()/hoursOnDisplay;
        content.setPreferredSize(new Dimension(w, (int)(24*(2*DayPanel.daysInBuffer + 1)*h)));
    }

    public void setDate(LocalDate date){
        content.setDateCentered(date);
        int sv = (scroll.getVerticalScrollBar().getMaximum() - scroll.getViewport().getHeight())/2;
        scroll.getVerticalScrollBar().setValue(sv);
    }

    void clearContent(){
        content.clearContent();
    }

    @Override
    public void languageChanged() {
        content.repaint();
    }
}
