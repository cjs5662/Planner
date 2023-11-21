package com.example.myapplicationllll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationllll.databinding.FragmentScheduleTodoBinding
import com.google.firebase.database.FirebaseDatabase

class Schedule_Todo : Fragment() {

    private lateinit var binding: FragmentScheduleTodoBinding
    private lateinit var viewModel: ScheduleTodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ScheduleTodoViewModel::class.java)

        // RecyclerView 설정
        val scheduleAdapter = ScheduleAdapter()
        binding.scheduleRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.scheduleRecyclerView.adapter = scheduleAdapter

        val todoAdapter = TodoAdapter()
        binding.todoRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.todoRecyclerView.adapter = todoAdapter

        viewModel.fetchScheduleAndTodo()
        viewModel.scheduleList.observe(viewLifecycleOwner) { scheduleDataList ->
            scheduleAdapter.submitList(scheduleDataList)
        }
        viewModel.todoList.observe(viewLifecycleOwner) { todoDataList ->
            todoAdapter.submitList(todoDataList)
        }

        // 할일, 일정 입력 화면으로 이동하는 버튼 클릭 리스너 설정
        binding.btnScheduleTodoInput.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frame, Schedule_Todo_Input())
            transaction.commit()
        }
    }
}

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    private val scheduleList = ArrayList<Schedule>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule = scheduleList[position]
        holder.bind(schedule)
    }

    override fun getItemCount() = scheduleList.size

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.is_title)
        private val time: TextView = view.findViewById(R.id.is_time)
        private val details: TextView = view.findViewById(R.id.is_details)
        private val reward: TextView = view.findViewById(R.id.is_reward)
        private val btnDelete: Button = view.findViewById(R.id.btn_delete_schedule)
        fun bind(schedule: Schedule) {
            title.text = schedule.title
            time.text = schedule.time
            details.text = schedule.details
            reward.text = schedule.reward

            // 삭제 버튼에 클릭 리스너 설정
            btnDelete.setOnClickListener {
                // Firebase에서 해당 항목을 삭제하는 코드
                val database = FirebaseDatabase.getInstance("https://planner-c04d6-default-rtdb.firebaseio.com/")
                val reference = database.getReference("schedules")
                reference.child(schedule.id).removeValue()
            }
        }
    }

    fun submitList(newScheduleList: List<Schedule>) {
        scheduleList.clear()
        scheduleList.addAll(newScheduleList)
        notifyDataSetChanged()
    }
}

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {
    private val todoList = ArrayList<Todo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = todoList[position]
        holder.bind(todo)
    }

    override fun getItemCount() = todoList.size

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.it_title)
        private val time: TextView = view.findViewById(R.id.it_time)
        private val details: TextView = view.findViewById(R.id.it_details)
        private val reward: TextView = view.findViewById(R.id.it_reward)
        private val btnDelete: Button = view.findViewById(R.id.btn_delete_todo)

        fun bind(todo: Todo) {
            title.text = todo.title
            time.text = todo.time
            details.text = todo.details
            reward.text = todo.reward

            // 삭제 버튼에 클릭 리스너 설정
            btnDelete.setOnClickListener {
                val database = FirebaseDatabase.getInstance("https://planner-c04d6-default-rtdb.firebaseio.com/")
                val reference = database.getReference("todos")
                reference.child(todo.id).removeValue()
            }
        }
    }

    fun submitList(newTodoList: List<Todo>) {
        todoList.clear()
        todoList.addAll(newTodoList)
        notifyDataSetChanged()
    }
}