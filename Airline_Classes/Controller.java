package utrupBrandon_Milestone1;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Controller {
	public String display() {
		return "index";
	}

}
