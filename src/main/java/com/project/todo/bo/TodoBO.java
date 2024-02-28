package com.project.todo.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.todo.Entity.TodoContentEntity;
import com.project.todo.Entity.TodoDayEntity;
import com.project.todo.mapper.TodoMapper;
import com.project.todo.repository.TodoContentRepository;
import com.project.todo.repository.TodoDayRepository;
import com.project.user.bo.UserBO;
import com.project.user.entity.UserEntity;

@Service
public class TodoBO {

	@Autowired
	private TodoContentRepository todoContentRepository;
	
	@Autowired
	private TodoDayRepository todoDayRepository;
	
	@Autowired
	private TodoMapper todoMapper;
	
	public TodoDayEntity addTodoDay(int userId, String todoDay) {
		
		return todoDayRepository.save(TodoDayEntity.builder()
				.userId(userId)
				.todoDay(todoDay)
				.build());
	}
	
	public List<TodoDayEntity> getTodoDayList() {
		return todoDayRepository.findAllByOrderByTodoDayDesc();
	}
	
	public List<TodoDayEntity> getTodoDayByUserIdList(int userId) {
		return todoDayRepository.findAllByUserIdOrderByTodoDayDesc(userId);
	}
	
	public TodoDayEntity getTodoDayAndUserIdByTodoDay(String todoDay, int userId) {
		return todoDayRepository.findByTodoDayAndUserId(todoDay, userId);
	}
	
	public TodoContentEntity addTodoContent(int userId, String todoDay,
			String content, boolean checkboxYn) {
		
		return todoContentRepository.save(TodoContentEntity.builder()
				.userId(userId)
				.todoDay(todoDay)
				.content(content)
				.checkBoxYn(checkboxYn)
				.build());
	}
	
	public List<TodoContentEntity> getTodoContentByuserId(int userId) {
		return todoContentRepository.findAllByUserId(userId);
	}
	
	public void createTodo(int userId, String todoDay,
			String content, boolean checkboxYn) {
		
		TodoDayEntity day = todoDayRepository.findByTodoDayAndUserId(todoDay,userId);
		
			if (day == null) {// 그룹테이블인 그 날짜가 있는지
				day = todoDayRepository.save(TodoDayEntity.builder() // save 하면서 리턴이 바로됨
						.userId(userId)
						.todoDay(todoDay)
						.build());
		
				todoContentRepository.save(TodoContentEntity.builder()
						.userId(userId)
						.dayId(day.getId())
						.todoDay(todoDay)
						.content(content)
						.checkBoxYn(checkboxYn)
						.build());
			} else {
				todoContentRepository.save(TodoContentEntity.builder()
						.userId(userId)
						.dayId(day.getId())
						.todoDay(todoDay)
						.content(content)
						.checkBoxYn(checkboxYn)
						.build());
			}
		}
	// 체크박스 유무 
	public void updateTodoByCheckboxYn(int userId, int contentId, boolean checkboxYn) {
			todoMapper.updateTodoByCheckboxYn(userId, contentId, checkboxYn);
	}
	
	// delete // todoDay 안에 해당하는 컨텐트개수가 0이라면 카드뷰전체 삭제
	public void deleteTodoContentByContentId(int contentId) {
		todoMapper.deleteTodoByContentId(contentId);
	}
	
}