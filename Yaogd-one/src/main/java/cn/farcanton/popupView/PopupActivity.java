package cn.farcanton.popupView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.farcanton.R;

public class PopupActivity extends Activity{
	
	/**
	* 系统菜单定制控件
	*/
    public HotalkMenuView menuListView = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_view_main);
		
    }
	/**
    * 系统菜单初始化 void
    */
    private void initSysMenu()
    {
        if (menuListView == null)
        {
            menuListView = new HotalkMenuView(this);
        }
        menuListView.listview.setOnItemClickListener(listClickListener);
        menuListView.clear();
        // 添加按位置添加
        menuListView.add(HotalkMenuView.MENU_SEND_MSG_FORMULAR, getString(R.string.currFormularStr));
        menuListView.add(HotalkMenuView.MENU_VIEW_CONTACT, getString(R.string.chat_search_card));
        menuListView.add(HotalkMenuView.MENU_ADD_CONTACT, getString(R.string.chat_menu_addtocontact));
        menuListView.add(HotalkMenuView.MENU_DELETE_MULTI_MSG, getString(R.string.chat_menu_delete_msg));
        menuListView.add(HotalkMenuView.MENU_ADD_TO_FAVORITES, getString(R.string.multi_favorites));
     }

    protected void switchSysMenuShow()
    {
        // 初始化系统菜单 
        initSysMenu();
        if (!menuListView.getIsShow())
        {
    
            menuListView.show();
        }
        else
        {
            menuListView.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add("menu");// 必须创建一项
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu){
  
        switchSysMenuShow();
    
        return false;// 返回为true 则显示系统menu
    
    }
    /**
     * 菜单点击事件处理
     */
    OnItemClickListener listClickListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3)
        {
            // 获取key唯一标识
            int key = Integer.parseInt(view.getTag().toString());

            // 跳转
            switch (key)
            {
                case HotalkMenuView.MENU_SEND_MSG_FORMULAR:
                    //editFormular();
                    break;
                case HotalkMenuView.MENU_VIEW_CONTACT:
                    break;
                case HotalkMenuView.MENU_ADD_CONTACT:
                    break;
                case HotalkMenuView.MENU_DELETE_MULTI_MSG:
                    break;
                case HotalkMenuView.MENU_ADD_TO_FAVORITES:
                    break;         
                default:
                    break;
            }
            // 关闭
            menuListView.close();
        }

    };
}
