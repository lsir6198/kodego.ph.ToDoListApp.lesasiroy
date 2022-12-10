package com.kodego.todolistapp


import android.graphics.Typeface
import android.os.Build
import android.text.AutoText
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.autofill.AutofillId
import androidx.annotation.RequiresApi
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.kodego.todolistapp.databinding.RowItemBinding
import java.time.Instant
import java.util.Date


class WorkListAdapter(var worklistModel: MutableList<WorkList>): RecyclerView.Adapter<WorkListAdapter.WorkListViewHolder>() {

        var onItemDelete : ((WorkList, Int) -> Unit) ? = null
        var onEdit : ((WorkList, Int) -> Unit) ? = null
        var onView : ((WorkList, Int) -> Unit) ? = null


        inner class WorkListViewHolder(var binding: RowItemBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RowItemBinding.inflate(layoutInflater, parent, false)
            return WorkListViewHolder(binding)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: WorkListViewHolder, position: Int) {
            holder.binding.apply{
                txtvwToDoWork.text = worklistModel[position].workList
                txtvwDate.text = Date.from(Instant.now()).toString()

                imgbtnDelete.setOnClickListener(){
                    onItemDelete?.invoke(worklistModel[position],position)
                }
                imgbtnEdit.setOnClickListener(){
                    onEdit?.invoke(worklistModel[position],position)
                }
                view.setOnClickListener(){
                        onView?.invoke(worklistModel[position],position)
                        txtvwToDoWork.setTypeface(Typeface.DEFAULT_BOLD, Typeface.NORMAL)
                        imgbtnEdit.isEnabled = false
                        txtvwToDoWork.isAllCaps = true
                        txtvwToDoWork.setText("Done")
                        view.isInvisible = true

                }
            }
        }

        override fun getItemCount(): Int {
            return worklistModel.size
        }

}






