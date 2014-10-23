import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Properties;

import java.io.StringWriter;
import java.io.PrintWriter;

public class ThesaurusServlet extends HttpServlet
{
    Thesaurus t;

    @Override
    public void init()
    {

        String basepath = "/home/n/programming/java/tomcat/apache-tomcat-7.0.53/webapps/demo/";
        try {
            Properties config = new Properties();
            FileInputStream in = new FileInputStream(basepath + "config");
            config.loadFromXML(in);
            in.close();
            this.t = new Thesaurus(config.getProperty("json_path"));
        } catch (Exception e) {
            this.log.log(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        StringBuffer jb = new StringBuffer();
        String line = null;
        try 
        {
            BufferedReader reader = request.getReader();
            while((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e)
        {
            this.log.log(e);
        }
        PrintWriter out = response.getWriter();
        try 
        {
            JSONObject j = new JSONObject(jb.toString());
            String start, end;
            start = j.getString("origin");
            end = j.getString("destination");
            JSONArray avoids = new JSONArray();
            if(j.has("avoids"))
            {
                avoids = j.getJSONArray("avoids");
            }
            out.println(this.t.synpath(start, end, avoids));
        } catch (Exception e) //doesn't happen
        {
            out.println("{'message': 'internal server error'}");
        }
    }
}
