package com.example.todolist.controller;

import com.example.todolist.controller.requestDto.SearchRequestDto;
import com.example.todolist.controller.requestDto.TodoRequestDto;
import com.example.todolist.domain.Member;
import com.example.todolist.domain.Todo;
import com.example.todolist.repository.MemberRepository;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TodoController {
    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;
    private final TodoService todoService;

    @GetMapping("/todolist/{id}")
    public String todolist(Model model, @PathVariable Long id) {
        Optional<Member> member = memberRepository.findById(id);
        model.addAttribute("member", member);
        model.addAttribute("todo", new TodoRequestDto());
        model.addAttribute("searches",new SearchRequestDto());
        return "todolist";
    }

    @PostMapping("/todolist/{id}")
    public String todoSubmit(@ModelAttribute TodoRequestDto todo, @PathVariable Long id) {
        Todo todo1 = Todo.builder()
                .member(memberRepository.findById(id).get())
                .content(todo.getContent())
                .build();
        todoRepository.save(todo1);
        return "redirect:" + id;
    }

    //할 일 삭제
    @GetMapping ("/todolist/delete")
    public String todoDelete(Long id ,Long member)  {
        System.out.println("삭제 에이피아이 : "+id);
            System.out.println("삭제 돼라");
            todoRepository.deleteById(id);

        return "redirect:"+member;
    }
    //투두 결과 페이지

    //할 일 검색
    @GetMapping("/todosearch")
    public String search(@ModelAttribute SearchRequestDto keyword ,Model model ){
        System.out.println("keyword : " + keyword.getKeyword());

       List<Todo> todoList=todoRepository.search(keyword.getKeyword());
        System.out.println(todoList);
       model.addAttribute("todolist",todoList);
       return "searchResult";
    }

    //할 일 중요여부
    @GetMapping("/todolist/todoStar")
    public String todoStar(Long id , Long member , boolean star) {
        todoService.todoStar(id, star);
        return "redirect:"+member;
    }

    //할 일 완료여부
    @GetMapping("/todolist/todoCompleted")
    public String todoCompleted(Long id , Long member ,boolean completed) {
        todoService.complete(id,completed);
        return "redirect:"+member;
    }



}