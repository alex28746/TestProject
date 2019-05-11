package client.frames;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleInsets;
import toneanalyzer.model.Emotion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.util.Map;

public class SummaryFrame extends ApplicationFrame {

    private static final long serialVersionUID = 1L;

    Color[] colors = {new Color(200, 200, 255), new Color(255, 200, 200),
            new Color(200, 255, 200), new Color(200, 255, 200)};

    static {
        // set a theme using the new shadow generator feature available in
        // 1.0.14 - for backwards compatibility it is not enabled by default
        ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow", true));
    }

    public SummaryFrame(Map<String, Map<Emotion, Double>> averageModelMap) {
        super("Summary");
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1));
        JScrollPane jScrollPane = new JScrollPane(jPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        for(String username : averageModelMap.keySet()) {
            jPanel.add(createUserDiagram(username, averageModelMap.get(username)));
        }
        jScrollPane.setViewportView(jPanel);

        setContentPane(jScrollPane);
        pack();
        setVisible(true);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        this.setVisible(false);
    }

    private PieDataset createDataset(Map<Emotion, Double> emotions) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        System.out.println(emotions.toString());
        for(Object emotion : emotions.keySet()) {
            try {
                dataset.setValue(emotion.toString(), emotions.get(emotion));
            } catch(Exception e) {
                System.out.println("Cannot convert map in SummaryFrame, " + e);
            }
        }
        return dataset;
    }

    private JFreeChart createChart(String username, PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                username,  // chart title
                dataset,             // data
                false,               // no legend
                true,                // tooltips
                false                // no URL generation
        );

        // Определение фона графического изображения
        chart.setBackgroundPaint(new GradientPaint(new Point(0, 0), new Color(20, 20, 20),
                new Point(400, 200), Color.DARK_GRAY));
        // Определение заголовка
        TextTitle t = chart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.LEFT);
        t.setPaint(new Color(240, 240, 240));
        t.setFont(new Font("Arial", Font.BOLD, 26));

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setInteriorGap(0.04);
        plot.setOutlineVisible(false);

        RadialGradientPaint rgpBlue;
        RadialGradientPaint rgpRed;
        RadialGradientPaint rgpGreen;
        RadialGradientPaint rgpYellow;

        rgpBlue = createGradientPaint(colors[0], Color.BLUE);
        rgpRed = createGradientPaint(colors[1], Color.RED);
        rgpGreen = createGradientPaint(colors[2], Color.GREEN);
        rgpYellow = createGradientPaint(colors[3], Color.YELLOW);

        // Определение секций круговой диаграммы
        plot.setSectionPaint("Оплата жилья", rgpBlue);
        plot.setSectionPaint("Школа, фитнес", rgpRed);
        plot.setSectionPaint("Развлечения", rgpGreen);
        plot.setSectionPaint("Дача, стройка", rgpYellow);
        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(2.0f));

        // Настройка меток названий секций
        plot.setLabelFont(new Font("Courier New", Font.BOLD, 20));
        plot.setLabelLinkPaint(Color.WHITE);
        plot.setLabelLinkStroke(new BasicStroke(2.0f));
        plot.setLabelOutlineStroke(null);
        plot.setLabelPaint(Color.WHITE);
        plot.setLabelBackgroundPaint(null);

        return chart;
    }

    private RadialGradientPaint createGradientPaint(Color c1, Color c2) {
        Point2D center = new Point2D.Float(0, 0);
        float radius = 200;
        float[] dist = {0.0f, 1.0f};
        return new RadialGradientPaint(center, radius, dist,
                new Color[]{c1, c2});
    }

    public JPanel createUserDiagram(String username, Map<Emotion, Double> emotions) {
        JFreeChart chart = createChart(username, createDataset(emotions));
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        // panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(600, 220));
        return panel;
    }
}

