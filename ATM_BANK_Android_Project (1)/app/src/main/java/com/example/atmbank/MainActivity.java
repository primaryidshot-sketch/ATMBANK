package com.example.atmbank;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends Activity {
    private final int bg = Color.rgb(7, 13, 29);
    private final int panel = Color.rgb(17, 27, 51);
    private final int blue = Color.rgb(45, 112, 255);
    private final int white = Color.rgb(245, 247, 255);
    private final int muted = Color.rgb(157, 172, 203);
    private long balance = 5000;
    private TextView balanceText, statusText;
    private LinearLayout root;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);
        showHome();
    }

    private GradientDrawable shape(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        return d;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private void showHome() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(22), dp(18), dp(22), dp(24));
        root.setBackgroundColor(bg);
        scroll.addView(root);
        setContentView(scroll);

        TextView brand = text("●  ATM BANK", 22, white, true);
        root.addView(brand, lp(-1, 48));
        TextView subtitle = text("YOUR MONEY, YOUR CONTROL", 11, muted, true);
        root.addView(subtitle, lp(-1, 25));

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(20), dp(20), dp(20), dp(22));
        card.setBackground(shape(panel, dp(22)));
        LinearLayout.LayoutParams cp = lp(-1, -2);
        cp.topMargin = dp(24);
        root.addView(card, cp);

        TextView label = text("AVAILABLE BALANCE", 12, muted, true);
        card.addView(label, lp(-1, 25));
        balanceText = text(money(balance) + " ฿", 34, white, true);
        LinearLayout.LayoutParams bp = lp(-1, 58);
        card.addView(balanceText, bp);
        TextView demo = text("●  DEMO ACCOUNT  •  NOT A REAL BANK", 10, muted, true);
        card.addView(demo, lp(-1, 28));

        TextView actions = text("Services", 20, white, true);
        LinearLayout.LayoutParams ap = lp(-1, 44);
        ap.topMargin = dp(22);
        root.addView(actions, ap);

        addAction("▣", "ตรวจสอบยอดเงิน", "ดูยอดเงินคงเหลือในบัญชี", () -> showBalance());
        addAction("+", "ฝากเงิน", "เพิ่มเงินเข้าบัญชีจำลอง", () -> amountDialog(true));
        addAction("−", "ถอนเงิน", "ถอนเงินจากบัญชีจำลอง", () -> amountDialog(false));

        statusText = text("ระบบพร้อมใช้งาน", 12, muted, false);
        statusText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams sp = lp(-1, 38);
        sp.topMargin = dp(10);
        root.addView(statusText, sp);

        Button exit = new Button(this);
        exit.setText("ออกจากระบบ");
        exit.setTextColor(white);
        exit.setAllCaps(false);
        exit.setBackground(shape(Color.rgb(35, 45, 68), dp(14)));
        root.addView(exit, lp(-1, 52));
        exit.setOnClickListener(v -> new AlertDialog.Builder(this)
            .setTitle("ออกจากระบบ")
            .setMessage("ต้องการปิดแอป ATM BANK หรือไม่?")
            .setNegativeButton("ยกเลิก", (d, w) -> {})
            .setPositiveButton("ออก", (d, w) -> finish())
            .show());

        TextView footer = text("ATM BANK  •  SIMULATION APP", 10, muted, true);
        footer.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams fp = lp(-1, 42);
        root.addView(footer, fp);
    }

    private void addAction(String icon, String title, String desc, Runnable action) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(14), dp(10), dp(12), dp(10));
        row.setBackground(shape(panel, dp(17)));
        LinearLayout.LayoutParams rp = lp(-1, 78);
        rp.topMargin = dp(10);
        root.addView(row, rp);

        TextView symbol = text(icon, 25, white, true);
        symbol.setGravity(Gravity.CENTER);
        symbol.setBackground(shape(blue, dp(12)));
        row.addView(symbol, lp(48, 48));

        LinearLayout words = new LinearLayout(this);
        words.setOrientation(LinearLayout.VERTICAL);
        words.setPadding(dp(12), 0, 0, 0);
        row.addView(words, new LinearLayout.LayoutParams(0, -2, 1));
        words.addView(text(title, 16, white, true), lp(-1, 27));
        words.addView(text(desc, 11, muted, false), lp(-1, 22));
        TextView arrow = text("›", 27, muted, true);
        row.addView(arrow, lp(24, 44));
        row.setOnClickListener(v -> action.run());
    }

    private void showBalance() {
        balanceText.setText(money(balance) + " ฿");
        statusText.setText("ตรวจสอบยอดเงินล่าสุดแล้ว");
        new AlertDialog.Builder(this)
            .setTitle("ยอดเงินคงเหลือ")
            .setMessage("คุณมีเงินในบัญชีจำลอง " + money(balance) + " บาท")
            .setPositiveButton("ตกลง", null)
            .show();
    }

    private void amountDialog(boolean deposit) {
        EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setTextColor(white);
        input.setHintTextColor(muted);
        input.setHint("ระบุจำนวนเงิน (บาท)");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setPadding(dp(14), dp(12), dp(14), dp(12));
        input.setBackground(shape(panel, dp(12)));

        LinearLayout wrap = new LinearLayout(this);
        wrap.setPadding(dp(22), dp(10), dp(22), dp(2));
        wrap.addView(input, lp(-1, 54));

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle(deposit ? "ฝากเงิน" : "ถอนเงิน")
            .setView(wrap)
            .setNegativeButton("ยกเลิก", null)
            .setPositiveButton("ยืนยัน", null)
            .create();

        dialog.setOnShowListener(x -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String value = input.getText().toString().trim();
            if (value.isEmpty()) {
                input.setError("กรุณากรอกจำนวนเงิน");
                return;
            }
            long amount;
            try {
                amount = Long.parseLong(value);
            } catch (NumberFormatException e) {
                input.setError("จำนวนเงินไม่ถูกต้อง");
                return;
            }
            if (amount <= 0) {
                input.setError("จำนวนเงินต้องมากกว่า 0");
                return;
            }
            if (!deposit && amount > balance) {
                input.setError("ยอดเงินไม่เพียงพอ");
                return;
            }
            if (deposit) balance += amount; else balance -= amount;
            balanceText.setText(money(balance) + " ฿");
            statusText.setText(deposit ? "ฝากเงินสำเร็จ" : "ถอนเงินสำเร็จ");
            dialog.dismiss();
            new AlertDialog.Builder(this)
                .setTitle(deposit ? "ฝากเงินสำเร็จ" : "ถอนเงินสำเร็จ")
                .setMessage((deposit ? "ฝากเงิน " : "ถอนเงิน ") + money(amount) +
                    " บาท\nยอดเงินคงเหลือ " + money(balance) + " บาท")
                .setPositiveButton("ตกลง", null)
                .show();
        }));
        dialog.show();
    }

    private String money(long value) {
        return NumberFormat.getNumberInstance(Locale.US).format(value);
    }

    private LinearLayout.LayoutParams lp(int w, int h) {
        return new LinearLayout.LayoutParams(w < 0 ? w : dp(w), h < 0 ? h : dp(h));
    }

    private int dp(int value) {
        return (int)(value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
