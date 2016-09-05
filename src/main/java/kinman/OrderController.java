package kinman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepo;

    @RequestMapping(value = "/api/v1/orders", method = RequestMethod.GET)
    public Map<String, Object> index(HttpServletRequest request) {
        Map<String, Object> results = new HashMap<String, Object>();

        List<Order> orders = orderRepo.findAll();

        results.put("orders", orders);

        return results;
    }

    @RequestMapping(value = "/api/v1/orders", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody Order input, HttpServletRequest request) {

        orderService.create(input);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(input.getId()).toUri());

        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    }

}
