import javax.swing.*;
import java.awt.*;

public class DateBox2 extends JPanel {
    String day;
    Color color;
    int width;
    int height;

    public DateBox2(String day, Color color, int width, int height) {
        this.day = day;
        this.color = color;
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
    }

    // paint를 재정의하면 컴포넌트에 내가 원하는 것을 그릴 수 있다.
    // 이를 이용해서 배경색과 글씨를 넣기
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 현재 컴포넌트의 크기를 가져와서 중앙에 배치하기 위해 계산
        int x = (getWidth() - width) / 2;
        int y = (getHeight() - height) / 2;

        g.setColor(color);
        // 꽉찬 상자를 넣는 것인데 위에서 설정한 색으로 배경색을 칠하는 것
        g.fillRect(x, y, width, height);

        // 글자의 너비와 높이를 계산하여 가운데 정렬
        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(day);
        int textHeight = fontMetrics.getHeight();
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height + textHeight) / 2;

        g.setColor(Color.yellow);
        // 글씨를 그리는 것인데 첫 번째 매개변수는 글씨의 내용
        // 2번째는 x축  3번째는 y축
        g.drawString(day, textX, textY);
    }
}
