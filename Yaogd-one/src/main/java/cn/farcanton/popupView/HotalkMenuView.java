package cn.farcanton.popupView;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.farcanton.R;



public class HotalkMenuView extends View
{
	/**
	 * 设置界面各个选项字体大小,菜单字体大小
	 */
	public static float SET_TYPE_TEXT_SIZE = 16;
	
	/**
	 * 屏幕分辨率
	 */
	public static float currentDensity;
	public static int currentDensityDpi;
	public static int currentWidthPixels;
	public static int currentHeightPixels;
	
    /**
     * 发送短信的模式 hotalk或者短信
     */
    public final static int MENU_SEND_MSG_FORMULAR = 0;

    /**
     * 查看联系人
     */
    public final static int MENU_VIEW_CONTACT = 1;

    /**
     * 添加联系人
     */
    public final static int MENU_ADD_CONTACT = 2;

    /**
     * 群成员管理
     */
    public final static int MENU_MEMBER_MANAGER = 3;

    /**
     * 批量收藏
     */
    public final static int MENU_ADD_TO_FAVORITES = 4;

    /**
     * 批量删除
     */
    public final static int MENU_DELETE_MULTI_MSG = 5;



    /**
     * 上下文
     */
    private Context mContext;

    /**
     * PopupWindow
     */
    private PopupWindow popWindow;

    /**
     * 显示View
     */
    private View popview;

    /**
     * listview
     */
    public ListView listview;

    /**
     * 填充数据集
     */
    public ArrayList<MenuItem> mitems = null;

    /**
     * 设置显示位置
     */
    RelativeLayout layout;

    /**
     * 横屏菜单距离最大高度
     */
    private int bottomLenght_h = 77;

    /**
     * 竖屏菜单距离最大高度
     */
    private int bottomLenght_v = 173;

    /**
     * 屏幕密度
     */
    private Display display;

    /**
     * 画板用于测字符宽度
     */
    private Paint paint = null;

    /**
     * 菜单中最在的字符宽度
     */
    private float maxWeith = 0;

    /**
     * 显示靠左边菜单的左边距
     */
    private int rightMenuLeft = 45;

    /**
     * 显示靠左边菜单的最大右边距
     */
    private int maxRightWeith = 44;

    /**
     * 显示靠左边菜单的最小右边距
     */
    private int minRightWeith = 140;

    /**
     * 菜单的最小左边距
     */
    private int maxLeftWeith = 88;

    /**
     * 菜单的最小左边距
     */
    private int minLeftWeith = 188;

    /**
     * 横屏菜单最小左边距
     */
    private int maxLeftWeith_h = 282;

    /**
     * 横屏菜单最小左边距
     */
    private int minLeftWeith_h = 371;

    /**
     * 菜单的背景和文字两边占间距
     */
    private int contentSpaceWeith = 38;

    /**
     * 打开Menu动画
     */
    private TranslateAnimation myMenuOpen;

    /**
     * 关闭Menu动画
     */
    private TranslateAnimation myMenuClose;

    /**
     * 打开Menu动画所用时间
     */
    private int menuOpenMillis = 500;

    /**
     * 关闭Menu动画 所用时间
     */
    private int menuCloseMillis = 400;

    /**
     * 弹出窗口播放Menu的动画
     */
    private final int MENU_OPEN_ANIM = 1;

    /**
     * 关闭Menu动画 后关闭窗口
     */
    private final int MENU_CLOSE_ANIM = 2;

    /**
     * 当前窗口handler
     */
    private MyHandler myHandler = new MyHandler();

    /**
     * 正在进行关闭Menu操作
     */
    private boolean isDismissing = false;

    class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg == null)
            {
                return;
            }
            super.handleMessage(msg);
            switch (msg.what)
            {
                case MENU_OPEN_ANIM:
                    startMenuOpenAnimation();
                    break;
                case MENU_CLOSE_ANIM:
                	if(popWindow!=null){
                    popWindow.dismiss();
                	}
                    isDismissing = false;
                    break;
            }
        }
    }

    public HotalkMenuView(Context context)
    {
        super(context);
        mContext = context;
        mitems = new ArrayList<MenuItem>();
        LayoutInflater layoutInflater = (LayoutInflater) (mContext).getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        // 获取自定义布局文件的视图
        popview = layoutInflater.inflate(R.layout.popup_view, null);
        listview = (ListView) popview.findViewById(R.id.hotalk_menu_listview);
        layout = (RelativeLayout) popview.findViewById(R.id.hotalk_menu_view_layout);
        adapter = new ItemTextListAdapter(mContext);
        popWindow = new PopupWindow(popview, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);//以PopupWindow弹出
        display = ((WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE)).getDefaultDisplay();
        initValue();
        layout.setOnClickListener(onclick);
        listview.setFocusable(false);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listview.setAdapter(adapter);
        listview.setFocusableInTouchMode(true);
        listview.setMinimumHeight(200);
        listview.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                // 正执行关闭Menu菜单操作将不重复操作
                if (!isDismissing)
                {
                    isDismissing = true;
                    if ((keyCode == KeyEvent.KEYCODE_MENU) && (popWindow.isShowing()))
                    {
                        close();
                    }
                    else if (((keyCode == KeyEvent.KEYCODE_BACK) && (popWindow.isShowing())))
                    {
                        close();
                    }
                }

                return false;
            }
        });
    }

    /**
     * 初始设置Menu位置参数
     */
    private void initValue()
    {
        paint = new Paint();
        rightMenuLeft = (int) (rightMenuLeft * currentDensity);
        maxLeftWeith = (int) (maxLeftWeith * currentDensity);
        minLeftWeith = (int) (minLeftWeith * currentDensity);
        maxRightWeith = (int) (maxRightWeith * currentDensity);
        minRightWeith = (int) (minRightWeith * currentDensity);
        bottomLenght_h = (int) (bottomLenght_h * currentDensity);
        bottomLenght_v = (int) (bottomLenght_v * currentDensity);
        contentSpaceWeith = (int) (contentSpaceWeith * currentDensity);
        maxLeftWeith_h = (int) (maxLeftWeith_h * currentDensity);
        minLeftWeith_h = (int) (minLeftWeith_h * currentDensity);
    }

    /**
     * 添加菜单项
     * 
     * @param key
     * @param value
     */
    public void add(int key, String value)
    {
        remove(key); 
        MenuItem item = new MenuItem(key, value);
        mitems.add(item);
        adapter.notifyDataSetChanged();

    }

    /**
     * 添加 菜单项
     * 
     * @param position 位置(必须是按0~n)添加
     * @param key
     * @param value
     */
    public void add(int position, int key, String value)
    {
        MenuItem item = new MenuItem(key, value);
        if (position == mitems.size())
        {
            mitems.add(position, item);

            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取文本最大长度
     */
    private void getContextMaxLength()
    {
        adapter.notifyDataSetChanged();
        if (mitems != null && mitems.size() > 0)
        {
            maxWeith = 0;
            if (Build.VERSION.SDK_INT >= 14)
            {
                // android 4.0手机当前字体大小自适
                TextView tv = new TextView(mContext);
                tv.setTextSize(SET_TYPE_TEXT_SIZE);
                float size = tv.getTextSize() / SET_TYPE_TEXT_SIZE;
                paint.setTextSize((int) ((size * 12) * currentDensity));
            }
            else
            {
                paint.setTextSize((int) ((SET_TYPE_TEXT_SIZE + 1) * currentDensity));
            }

            for (int i = 0; i < mitems.size(); i++)
            {
                if (paint.measureText(mitems.get(i).MenuValue) > maxWeith)
                {
                    maxWeith = paint.measureText(mitems.get(i).MenuValue);
                }
            }
        }
    }

    /**
     * 更新菜单项按位置更新
     * 
     * @param position
     * @param key
     * @param value
     */
    public void updateItem(int position, int key, String value)
    {
        if (mitems.size() > position)
        {
            remover(position);
            MenuItem item = new MenuItem(key, value);
            mitems.add(position, item);
        }
    }

    /**
     * 删除菜单项
     * 
     * @param position
     */
    public void remover(int position)
    {
        if (mitems.size() > position)
        {
            mitems.remove(position);
        }
    }

    /**
     * 删除菜单项 add by shaxinajun
     * 
     * @param position
     */
    public void remove(int key)
    {
        MenuItem item = null;
        for (int i = 0; i < mitems.size(); i++)
        {
            item = mitems.get(i);
            if (item.MenuKey == key)
            {
                mitems.remove(i);
                break;
            }
        }
    }

    /**
     * 清空菜单项
     */
    public void clear()
    {
        mitems.clear();
        maxWeith = 0;
    }

    ItemTextListAdapter adapter;

    /**
     * 设置Menu显示位置，不可以自适应屏幕 密度 方法表述
     * 
     * @param left
     * @param top
     * @param right
     * @param bottom void
     */
    public void setMenuPosition(int left, int top, int right, int bottom)
    {
        layout.setPadding(left, (int) (46 * currentDensity), right, bottom);
    }

    /**
     * 单击事件
     */
    private OnClickListener onclick = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
            // 点击空白外让其消失
                case R.id.hotalk_menu_view_layout:
                    close();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 重新设置菜单项
     * 
     * @param list
     */
    public void setItems(ArrayList<String> items)
    {
        mitems.clear();
        if (items != null && items.size() > 0)
        {
            for (int i = 0; i < items.size(); i++)
            {
                MenuItem item = new MenuItem(0, items.get(i));
                mitems.add(item);
            }
        }
    }

    public void setPositionShow()
    {
        try
        {
            if (popWindow != null && popview != null)
            {
                if (popWindow.isShowing())
                {
                    startMenuCloseAnimation();
                }
                else
                {
                    getContextMaxLength();
                   int right = (int) ((320 * currentDensity) - (maxWeith + rightMenuLeft + contentSpaceWeith));
                 
                    if (right < maxRightWeith)
                    {
                        right = maxRightWeith;
                    }
                    else if (right > minRightWeith)
                    {
                        right = minRightWeith;
                    }
                    //setMenuPosition(rightMenuLeft, 10, right, bottomLenght_v);
                    setMenuPosition(rightMenuLeft, 0, 0, bottomLenght_v);
                    Collections.sort(mitems);
                    // 规定弹窗的位置
                    popWindow.setFocusable(true);
                    popWindow.update();
                    popWindow.showAtLocation(popview, Gravity.FILL, 0, 0);
                    myHandler.sendEmptyMessage(MENU_OPEN_ANIM);
                }
            }
        }
        catch (Exception e)
        {
            Log.i("HotalkMenuView", "e:" + e.toString());
            close();
        }
    }

    /**
     * 显示
     */
    public void show()
    {
        try
        {
            if (popWindow != null && popview != null)
            {
                if (popWindow.isShowing())
                {
                    startMenuCloseAnimation();
                }
                else
                {
                    if (mitems != null && mitems.size() > 0)
                    {
                        int orientation = display.getOrientation();
                        if (orientation == 0)
                        {// 竖屏
                        	Activity act = (Activity)mContext ;
                            if ( act != null
                                    && act.getCurrentFocus() != null
                                    && act.getCurrentFocus().getWindowToken() != null)
                            {
                                ((InputMethodManager) act
                                        .getSystemService(act.INPUT_METHOD_SERVICE))
                                        .hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
                            }
                            getContextMaxLength();
                            int left = (int) ((320 * currentDensity) - (maxWeith + contentSpaceWeith));
                            if (left < maxLeftWeith)
                            {
                                left = maxLeftWeith;
                            }
                            else if (left > minLeftWeith)
                            {
                                left = minLeftWeith;
                            }
                            setMenuPosition(left, 0, 0, bottomLenght_v);

                        }
                        else
                        // 横屏
                        {
                            getContextMaxLength();
                            int left = (int) ((533 * currentDensity) - (maxWeith + contentSpaceWeith));
                            Log.i("jindegege", "left:" + left + " rightMenuLeft:"
                                    + (480 * currentDensity));
                            if (left < maxLeftWeith_h)
                            {
                                left = maxLeftWeith_h;
                            }
                            else if (left > minLeftWeith_h)
                            {
                                left = minLeftWeith_h;
                            }
                            setMenuPosition(left, 0, 0, bottomLenght_h);
                        }
                        Collections.sort(mitems);
                        // 规定弹窗的位置
                        popWindow.setFocusable(true);
                        popWindow.update();
                        popWindow.showAtLocation(listview, Gravity.FILL, 0, (int) (46 * currentDensity));
                        myHandler.sendEmptyMessage(MENU_OPEN_ANIM);

                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.i("HotalkMenuView", "show() e:" + e.toString());
            close();
        }
    }

    /**
     * 判读是否显示
     * 
     * @return boolean
     */
    public boolean getIsShow()
    {
        return popWindow.isShowing();
    }

    /**
     * 关闭
     */
    public void close()
    {
        if (popWindow != null && popWindow.isShowing())
        {
            startMenuCloseAnimation();
        }
    }

    /**
     * 打开menu菜单窗口动画
     */
    private void startMenuOpenAnimation()
    {
        // 由于打开菜单高度不一至所以根据菜单的高度来设置打开菜单时间
        menuOpenMillis = (mitems.size() * 100) + 100;
        if (menuOpenMillis > 500)
        {
            menuOpenMillis = 500;
        }
        myMenuOpen = new TranslateAnimation(0f, 0f, -(listview.getHeight() + 1), 0f);
        myMenuOpen.setDuration(menuOpenMillis);
        listview.startAnimation(myMenuOpen);
    }

    /**
     * 关闭menu菜单窗口动画
     */
    private void startMenuCloseAnimation()
    {
        myMenuClose = new TranslateAnimation(0f, 0f, 0f, -(listview.getHeight() + 1));
        myMenuClose.setDuration(menuCloseMillis);
        listview.startAnimation(myMenuClose);
        myHandler.sendEmptyMessageDelayed(MENU_CLOSE_ANIM, menuCloseMillis);
    }

    public class ItemTextListAdapter extends SimpleAdapter
    {

        public ItemTextListAdapter(Context context)
        {
            super(context, null, 0, null, null);
        }

        @Override
        public int getCount()
        {
            return mitems.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            ItemHolder holder;
            if (convertView == null || convertView.getTag() == null || !(convertView.getTag() instanceof ItemHolder))
            {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.popup_window_item, null, true);
                holder = new ItemHolder();
                holder.menuName = (TextView) convertView.findViewById(R.id.pop_tv_item);
            }
            else
            {
                holder = (ItemHolder) convertView.getTag(R.layout.popup_window_item);
            }
            convertView.setTag(holder);
            convertView.setTag(R.layout.popup_window_item, holder);
            MenuItem item = mitems.get(position);
            holder.menuName.setText(item.MenuValue);
            holder.menuName.setTextSize(SET_TYPE_TEXT_SIZE);
            convertView.setTag(item.MenuKey);
            return convertView;
        }
    }

    public static class ItemHolder
    {
        TextView menuName;
    }

    public class MenuItem implements Comparable
    {
        int MenuKey;

        String MenuValue;

        public MenuItem(int key, String value)
        {
            MenuKey = key;
            MenuValue = value;
        }

        @Override
        public int compareTo(Object o)
        {
            // TODO Auto-generated method stub
            return this.MenuKey - ((MenuItem) o).MenuKey;
        }
    }
}
