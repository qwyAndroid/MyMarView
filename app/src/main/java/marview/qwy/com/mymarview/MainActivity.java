package marview.qwy.com.mymarview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import marview.qwy.com.marview.MarqueeView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MarqueeView m = (MarqueeView) findViewById(R.id.marqueeView);
        final MarqueeView m4 = (MarqueeView) findViewById(R.id.marqueeView4);

        List<String> info = new ArrayList<>();
        info.add("1. 大家好，我是qwy。");
        info.add("2. 欢迎大家关注我哦！");
        info.add("3. GitHub帐号：qwyAndroid");
        info.add("4. 知乎qwy");
        info.add("5. hahaha");
        info.add("6. 我要");
        m.startWithList(info);

        m4.startWithText("心中有阳光，脚底有力量！心中有阳光，脚底有力量！心中有阳光，脚底有力量！心中有阳光，脚底有力量！心中有阳光，脚底有力量！");

        m.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Toast.makeText(getApplicationContext(),textView.getText()+"",Toast.LENGTH_SHORT).show();
            }
        });

        m4.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Toast.makeText(getApplicationContext(),String.valueOf(m4.getPosition())+"."+textView.getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
