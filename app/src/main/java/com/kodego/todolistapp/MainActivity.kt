package com.kodego.todolistapp


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodego.todolistapp.databinding.AboutappBinding
import com.kodego.todolistapp.databinding.ActivityMainBinding
import com.kodego.todolistapp.databinding.EditDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var toDoListdb : ToDoDatabase
    lateinit var adapter : WorkListAdapter

    var calendar: Calendar = Calendar.getInstance()
    var simpleDateFormat = SimpleDateFormat("EEEE, MM-dd-yyyy hh:mm:ss")
    var dateTime = simpleDateFormat.format(calendar.time)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toDoListdb = ToDoDatabase.invoke(this)

        //display table data on screen
        view()

        var textView: TextView = binding.edtxtDate
        textView.setText(dateTime)
        textView.isEnabled = false

        binding.btnAdd.setOnClickListener(){
            var worklist:String = binding.edtxtToDoWork.text.toString()
            val workList = WorkList(worklist)


            add(workList)
            adapter.worklistModel.add(workList)
            binding.edtxtToDoWork.text.clear()

            adapter.notifyDataSetChanged()

            Toast.makeText(applicationContext,"ADDED!", Toast.LENGTH_LONG).show()
        }

        binding.btnReset.setOnClickListener() {
            binding.btnView.isInvisible = false
            binding.btnReset.isInvisible = true
            adapter.worklistModel.clear()
            binding.videoView.isVisible = true
            clearAllTask()
            adapter.notifyDataSetChanged()
            }

        binding.btnView.setOnClickListener() {
            binding.btnReset.isVisible = true
            binding.btnView.isVisible = false
            binding.videoView.isVisible = false
            view()
        }

        binding.vwAboutApp.setOnClickListener(){

            val dialogBox = Dialog(this)
            val binding: AboutappBinding = AboutappBinding.inflate(layoutInflater)
            dialogBox.setContentView(binding.root)
            dialogBox.show()

            binding.btnOKAY.setOnClickListener(){
                dialogBox.dismiss()
            }
        }
    }


    private fun delete(workList: WorkList){
        GlobalScope.launch(Dispatchers.IO) {
            toDoListdb.getWorklist().deleteWorkList(workList.listNumber)
            view()
        }
    }

    private fun clearAllTask(){
        GlobalScope.launch(Dispatchers.IO){
            toDoListdb.getWorklist().clearAllTask()
        }
    }

    private fun view() {
        lateinit var workList: MutableList<WorkList>
        GlobalScope.launch(Dispatchers.IO) {
            workList = toDoListdb.getWorklist().getAllWorkList()

            withContext(Dispatchers.Main){
                adapter = WorkListAdapter(workList)
                binding.rcyclrViewToDoList.adapter = adapter
                binding.rcyclrViewToDoList.layoutManager = LinearLayoutManager(applicationContext)

                adapter.onItemDelete = { item:WorkList, position: Int ->

                    delete(item)
                    adapter.worklistModel.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
                adapter.onEdit =  { item:WorkList, position: Int ->

                    showUpdateDialog(item.listNumber)
                    adapter.notifyDataSetChanged()
                }

            }
        }

    }


    private fun add(workList: WorkList) {
        GlobalScope.launch(Dispatchers.IO) {
            toDoListdb.getWorklist().addWork(workList)
            view()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showUpdateDialog(listNumber:Int) {
        val dialog = Dialog(this)
        val binding: EditDialogBinding = EditDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.show()

        binding.idBtnPickDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    binding.idTVSelectedDate.text =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }

        binding.btnOK.setOnClickListener(){
            var newWorkList :String = binding.etToDoWork.text.toString()

            GlobalScope.launch(Dispatchers.IO) {
                toDoListdb.getWorklist().updateWorkList(newWorkList,listNumber)
                view()
            }
            dialog.dismiss()
        }

    }

}






