package Ninja.coder.Ghostemane.code.adapter;

import Ninja.coder.Ghostemane.code.AnimUtils;
import Ninja.coder.Ghostemane.code.R;
import Ninja.coder.Ghostemane.code.adapter.DevAd;
import Ninja.coder.Ghostemane.code.model.DevModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DevAd extends RecyclerView.Adapter<DevAd.VH> {
  private List<DevModel> listDev;
  private OnItemClick click;

  public DevAd(List<DevModel> listDev, OnItemClick click) {
    this.listDev = listDev;
    this.click = click;
  }

  @Override
  public int getItemCount() {
    return listDev.size();
  }

  @Override
  public void onBindViewHolder(VH holder, int pos) {
    var model = listDev.get(pos);
    holder.dev_user.setText(model.getName());
    holder.dev_gouid.setText(model.getUser());
    holder.itemView.setOnClickListener(x ->{
      if(click != null) {
      	click.onItemChange(x,holder.getAdapterPosition());
        AnimUtils.Sacla(x);
      }
    });
  }

  @Override
  public VH onCreateViewHolder(ViewGroup pa, int arg1) {
    return new VH(
        LayoutInflater.from(pa.getContext()).inflate(R.layout.___layout_dev_libs, pa, false));
  }

  class VH extends RecyclerView.ViewHolder {
    protected TextView dev_user, dev_gouid;

    public VH(View view) {
      super(view);
      dev_gouid = view.findViewById(R.id.dev_gouid);
      dev_user = view.findViewById(R.id.dev_user);
      
    }
  }

  public interface OnItemClick {
    public void onItemChange(View v, int pos);
  }
}
