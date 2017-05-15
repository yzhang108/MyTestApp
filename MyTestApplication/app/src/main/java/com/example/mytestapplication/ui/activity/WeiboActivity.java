package com.example.mytestapplication.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytestapplication.R;
import com.example.mytestapplication.model.TopicEntity;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 张艳 on 2016/9/12.
 */
public class WeiboActivity extends Activity {

    private static final String AT = "@[\u4e00-\u9fa5\\w]+";// @人
    private static final String TOPIC = "#[\u4e00-\u9fa5\\w]+#";// ##话题
    private static final String EMOJI = "\\[[\u4e00-\u9fa5\\w]+\\]";// 表情
    private static final String URL = "http://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";// url

    private Button bt_add, bt_display;
    private EditText et_input;
    private TextView tv_display;
    private ArrayList<TopicEntity> mList = new ArrayList<TopicEntity>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weibo_activity);
        initView();
        initEvent();
    }

    private void initView() {
        bt_add = (Button) this.findViewById(R.id.bt_add_theme);
        et_input = (EditText) this.findViewById(R.id.et_weibotest);
        tv_display = (TextView) this.findViewById(R.id.tv_display_content);
        bt_display = (Button) this.findViewById(R.id.bt_display);
    }

    private void addNewTipic(boolean isStartFlag) {
        String addText = generatorNewTheme(isStartFlag);
        int fstart = et_input.getSelectionStart();
        int fend = fstart + addText.length();
        SpannableStringBuilder style = (SpannableStringBuilder) et_input.getText();

        style.insert(fstart, addText);
        style.setSpan(new ForegroundColorSpan(Color.RED), fstart, fend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        et_input.setText(style);
        et_input.setSelection(fend);
    }

    private void initEvent() {
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTipic(true);
            }
        });

        et_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {

                    int selectionStart = et_input.getSelectionStart();
                    int selectionEnd = et_input.getSelectionEnd();
                    Log.e("zy", "selectionStart=" + selectionStart + ",selectionEnd=" + selectionEnd);
                    //如果光标起始和结束在同一位置,说明是选中效果,直接返回 false 交给系统执行删除动作
                    if (selectionStart != selectionEnd) {
                        Log.e("zy", "删除一个话题");
                        deleteSelectedTopic();
                        return false;
                    }

                    Editable editable = et_input.getText();
                    String content = editable.toString();
                    int lastPos = 0;
                    int size = mList.size();
                    //遍历判断光标的位置
                    for (int i = 0; i < size; i++) {
                        mList.get(i).setSelected(false);
                        String topic = mList.get(i).getBookName();
                        lastPos = content.indexOf(topic, lastPos);
                        Log.e("zy", "i=" + i + ",lastpos=" + lastPos + ",topic=" + topic);
                        if (lastPos != -1) {
                            if (selectionStart != 0 && selectionStart > lastPos && selectionStart <= (lastPos + topic.length())) {
                                //选中话题
                                final int aaa = lastPos;
                                final int bbb = topic.length();
                                et_input.clearFocus();
                                et_input.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        et_input.setSelection(aaa, aaa + bbb);
                                        Log.e("zy", "设置选中话题,start=" + aaa + ",end=" + (aaa + bbb) + ",newSelectionStart=" + et_input.getSelectionStart() + ",newSelectionEnd=" + et_input.getSelectionEnd());
                                    }
                                });
                                mList.get(i).setSelected(true);
                                return true;
                            }
                        } else if (lastPos >= selectionStart) {
                            return false;
                        } else {
                            lastPos += topic.length();
                        }
                    }
                }
                return false;
            }

        });
        et_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", ((EditText) v).getSelectionStart() + "");
                int selectionStart = ((EditText) v).getSelectionStart();
                int lastPos = 0;
                for (int i = 0; i < mList.size(); i++) {
//                    Log.e("zy","i="+i+",mList.get(i).getBookName()="+mList.get(i).getBookName()+",mList.get(i).getBookName().length()="+mList.get(i).getBookName().length());
                    if ((lastPos = et_input.getText().toString().indexOf(mList.get(i).getBookName(), lastPos)) != -1) {
                        Log.i("zy", "lastPos=" + lastPos + "selectionStart=" + selectionStart);
                        if (selectionStart > lastPos && selectionStart <= (lastPos + mList.get(i).getBookName().length())) {
                            Log.e("zy", "设置光标在话题之后");
                            et_input.setSelection(lastPos + mList.get(i).getBookName().length());
                            break;
                        } else if (selectionStart < lastPos) {
                            Log.e("zy", "可以终止判断了吧");
                            break;
                        } else {
                            Log.e("zy", "判断不是一个话题,selectionStart=" + selectionStart + ",lastPos=" + lastPos + ",(lastPos + mList.get(i).getBookName().length())=" + (lastPos + mList.get(i).getBookName().length()));
                        }
                    } else {
                        lastPos += mList.get(i).getBookName().length();
                        Log.e("zy", "i=" + i + ",lastPos=" + lastPos);
                    }
                }
            }
        });
        bt_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_input.getText().toString();
                tv_display.setText(content);

                setTopicSpan(tv_display, content);
            }
        });
        et_input.addTextChangedListener(textWatcher);

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.e("zy","");
            if (s.equals("#")) {
                //展示添加话题对话框
                et_input.post(new Runnable() {
                    @Override
                    public void run() {
                        addNewTipic(false);
                    }
                });
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private String generatorNewTheme(boolean isStartWidthTag) {
        int nextInt = new Random().nextInt(100);
        String str = "";
        if (isStartWidthTag) {
            str += "#";
        }
        str += "测试测试" + nextInt + "# ";
        mList.add(new TopicEntity(nextInt, str));
        return str;
    }

    private void deleteSelectedTopic() {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isSelected()) {
                mList.remove(i);
            }
        }
    }


    public static void setTopicSpan(TextView textView, String content) {
        SpannableString msgSpan = new SpannableString(content);
        // 参数1 #话题#使用的正则表达式,正则验证网站 ： http://tool.chinaz.com/regex/
        String topicPatternStr = "#(.+?)#";

        // 参数2 使用正则表达式创建查询对象
        Pattern topicPattern = Pattern.compile(topicPatternStr);

        // 参数3 使用正则查找出msgSpan里所有的话题
        Matcher matcher = topicPattern.matcher(msgSpan);
        while (matcher.find()) {
            // matcher.group() 的返回值为匹配出来的字符串
            String topicName = matcher.group();
            Log.e("zy", "start=" + matcher.start() + ";end=" + matcher.end()
                    + ";话题为：=" + topicName);

            // 自定义的ClickableSpan，构造方法里接收话题字符串
            ClickableSpan clickableSpan = new TopicClickableSpan(topicName);
            // 设置点击
            msgSpan.setSpan(clickableSpan, matcher.start(), matcher.end(),
                    SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
            msgSpan.setSpan(new ForegroundColorSpan(Color.RED), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        }

        textView.setText(msgSpan);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private static class TopicClickableSpan extends ClickableSpan {

        private String topic;

        /**
         * 由于ClickableSpan的onClick方法参数为View，无法区分出被点击的span，只能在构造方法里传递话题字符串以供区分
         */
        private TopicClickableSpan(String topic) {
            this.topic = topic;
        }

        public void onClick(View widget) {
            Toast.makeText(widget.getContext(), "被点击的话题是:" + topic, Toast.LENGTH_SHORT).show();
        }
    }

    ;
}
