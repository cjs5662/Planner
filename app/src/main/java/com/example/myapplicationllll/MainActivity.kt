package com.example.myapplicationllll

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 기본 프래그먼트 설정
        supportFragmentManager.beginTransaction().replace(R.id.main_frame, planner()).commit()

        // 바텀네비게이션 설정
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                // 첫번째 버튼으로 홈 화면으로 이동
                R.id.nav_main -> {
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.main_frame, planner())
                    transaction.commit()
                }
                // 두 번째 버튼으로 일정, 할일 리스트 화면으로 이동
                R.id.nav_schedule_todo -> {
                    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.main_frame, ScheduleTodo())
                    transaction.commit()
                }
                R.id.nav_goal -> {
                    startActivity(Intent(this, GoalManagementActivity::class.java))
                }
            }
            true
        }

    }
}