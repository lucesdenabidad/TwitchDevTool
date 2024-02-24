package cc.datta.twitchdevtool.utils.scoreboard;

import java.util.List;

public class CustomScoreboard {
   private String title;
   private List<String> lines;

   public CustomScoreboard(String title, List<String> lines) {
      this.title = title;
      this.lines = lines;

      while(this.lines.size() > 15) {
         this.lines.remove(this.lines.size() - 1);
      }

   }

   public String getTitle() {
      return this.title;
   }

   public List<String> getLines() {
      return this.lines;
   }
}

