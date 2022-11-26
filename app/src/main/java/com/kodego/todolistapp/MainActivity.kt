package com.kodego.todolistapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kodego.todolistapp.databinding.ActivityMainBinding
import com.kodego.todolistapp.databinding.EditDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var toDoListdb : ToDoDatabase
    lateinit var adapter : WorkListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toDoListdb = ToDoDatabase.invoke(this)

        //display table data on screen
        view()

        binding.btnAdd.setOnClickListener(){
            var worklist:String = binding.edtxtToDoWork.text.toString()

            val workList = WorkList(worklist)
            add(workList)
            adapter.worklistModel.add(workList)
            adapter.notifyDataSetChanged()

            Toast.makeText(applicationContext,"ADDED!", Toast.LENGTH_LONG).show()
        }

        binding.btnReset.setOnClickListener(){
            adapter.worklistModel.clear()
            adapter.notifyDataSetChanged()

        }

    }

    private fun delete(workList: WorkList){
        GlobalScope.launch(Dispatchers.IO) {
            toDoListdb.getWorklist().deleteWorkList(workList.number)
            view()
        }
    }


    private fun view() {
        lateinit var workList: MutableList<WorkList>
        GlobalScope.launch(Dispatchers.IO) {
            workList = toDoListdb.getWorklist().getWorkList()

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

                    showUpdateDialog(item.number)
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

    private fun showUpdateDialog(id:Int) {
        val dialog = Dialog(this)
        val binding: EditDialogBinding = EditDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.show()

        binding.btnOK.setOnClickListener(){
            var newWorkList :String = binding.etToDoWork.text.toString()
            GlobalScope.launch(Dispatchers.IO) {
                toDoListdb.getWorklist().updateWorkList(newWorkList, number = 0)
                view()
            }
            dialog.dismiss()
        }

    }

}