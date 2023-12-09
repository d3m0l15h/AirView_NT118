package uit.com.airview.util;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

public class StrokeTextView extends androidx.appcompat.widget.AppCompatTextView {

    public StrokeTextView(Context context) {
        super(context);
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final Paint paint = this.getPaint();
        paint.setStyle(Paint.Style.STROKE);
        float strokeWidth = 10.0f;
        paint.setStrokeWidth(strokeWidth);
        int strokeColor = Color.BLACK;
        this.setTextColor(strokeColor);
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.FILL);
        this.setTextColor(Color.WHITE);
        super.onDraw(canvas);
    }
}