package www.project.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import www.project.service.MailService;
import www.project.service.UserService;
import www.project.domain.UserVO;

@Controller
@RequestMapping("/user/*")
@RequiredArgsConstructor
@Slf4j
public class userController {


    private final PasswordEncoder passwordEncoder;
    private final UserService usv;
    private final MailService msv;

    @GetMapping("/myPage")
    public void myPage(){}

    @GetMapping("/join")
    public void join(){}

    @PostMapping("/join")
    public String joinUser(UserVO uvo){
        uvo.setPw(passwordEncoder.encode(uvo.getPw()));
        usv.joinUser(uvo);
        return "/user/login";
    }

    @PostMapping(value="/nick",consumes = "text/plain", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> duplicationNick(@RequestBody String nickName){
        int isOk = usv.duplicationNick(nickName);
        return isOk==0? new ResponseEntity<String>("0", HttpStatus.OK):
                new ResponseEntity<String>("1",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(value="/email",consumes = "text/plain", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> duplicationEmail(@RequestBody String email){
        log.info("email>>>>{}",email);
        int isOk = usv.duplicationEmail(email);
        return isOk==0? new ResponseEntity<String>("0", HttpStatus.OK):
                new ResponseEntity<String>("1",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/login")
    public String login(@RequestParam(name = "error", required = false) String error, Model model) {
        if(error != null){
            model.addAttribute("errorMessage", "아이디와 비밀번호를 확인해주세요.");
            return "/user/login";
        }else{
            return "/user/login";
        }
    }

    @GetMapping("/findUser")
    public void findUser(){}

    //이메일 찾기
    @PostMapping("/find/{nick}")
    @ResponseBody
    public ResponseEntity<UserVO> findUserEmail(@PathVariable("nick") String nick){
        UserVO uvo = usv.findEmail(nick);
        return uvo==null? ResponseEntity.ok(null):ResponseEntity.ok(uvo);
    }

    //비밀번호 찾기
    @PostMapping("/find/{nick}/{email}")
    @ResponseBody
    public String findUserPw(@PathVariable("nick")String nick, @PathVariable("email")String email){
        if(usv.findUserPw(nick,email)>0){
            msv.sendNewPw(email);
            return "1";
        }
        return "0";
    }

    //이메일 인증발송
    @PostMapping("/email/verification/{email}")
    @ResponseBody
    public void sendMail(@PathVariable("email")String email){
        msv.sendMail(email);
    }

    //인증번호 확인
    @PostMapping("/email/{number}")
    @ResponseBody
    public String verificationEmail(@PathVariable("number")int number){
        Boolean isCorrect = msv.verificationEmail(number);
        return isCorrect ? "1":"0";
    }

}
