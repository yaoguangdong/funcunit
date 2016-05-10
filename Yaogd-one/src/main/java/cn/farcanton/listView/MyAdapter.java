package cn.farcanton.listView;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.farcanton.R;

public class MyAdapter extends BaseAdapter {

	private List<HashMap<String,String>> data;
	private LayoutInflater mInflater;
	public MyAdapter(Context context,List<HashMap<String,String>> data){
		this.data = data;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_view_in_scroll_view_item, null);
            holder = new ViewHolder();
            holder.TextView01 = (TextView)convertView.findViewById(R.id.TextView01);
            holder.TextView02 = (TextView)convertView.findViewById(R.id.TextView02);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.TextView01.setText(data.get(position).get("name").toString());
        holder.TextView02.setText(data.get(position).get("ip").toString());
        if (data.size() == 1) {
            convertView.setBackgroundResource(R.drawable.circle_list_single);
        } else if (data.size() > 1) {
            if (position == 0) {
                convertView.setBackgroundResource(R.drawable.circle_list_top);
            } else if (position == (data.size() )) {
                convertView.setBackgroundResource(R.drawable.circle_list_bottom);
            } else {
                convertView.setBackgroundResource(R.drawable.circle_list_middle);
            }
        }
        return convertView;
	}
	class ViewHolder {
        TextView TextView01;
        TextView TextView02;
    }
}
