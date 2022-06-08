package edu.asu.diging.vspace.web.staff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ListDownloadsController {

    @RequestMapping("/staff/downloads/list")
    public String listDownloads(Model model) {
        
        return "staff/downloads/downloadList";
    }
    
    
    @RequestMapping(value = "/staff/download", method = RequestMethod.GET) 
    public void downloadExhibition(Model model) {
        try {
            download("vspace/exhibit/space/SPA000000002");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public  void download(String urlString) throws IOException {
        URL url = new URL(urlString);
        try(
           BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
           BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/prachikharge/virtual-spaces-2.0/vspace/uploads/downloadTrial.html"));
        ) {
           String line;
           while ((line = reader.readLine()) != null) {
              writer.write(line);
           }
           System.out.println("Page downloaded.");
        }
     }
}
