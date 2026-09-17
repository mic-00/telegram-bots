package it.michelemasetto.telegram.bots.sqlclient;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import it.michelemasetto.telegram.bots.TelegramAbilityBot;
import it.michelemasetto.telegram.bots.Util;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.abilitybots.api.objects.Locality;
import org.telegram.telegrambots.abilitybots.api.objects.Privacy;
import org.telegram.telegrambots.meta.api.methods.send.SendRichMessage;
import org.telegram.telegrambots.meta.api.objects.richtext.InputRichMessage;

import java.io.StringWriter;
import java.io.Writer;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@Slf4j
public class SqlClientBot extends TelegramAbilityBot {

    private static final String SQL_CLIENT_BOT_TOKEN = "SQL_CLIENT_BOT_TOKEN";

    private static final String SQL_CLIENT_BOT_USERNAME = "SQL_CLIENT_BOT_USERNAME";

    private final Configuration cfg;

    private final Map<Long, SqlClientProperties> map;

    public SqlClientBot() {
        super(System.getenv(SQL_CLIENT_BOT_TOKEN), System.getenv(SQL_CLIENT_BOT_USERNAME));
        cfg = new Configuration(Configuration.VERSION_2_3_35);
        cfg.setTemplateLoader(new ClassTemplateLoader(SqlClientBot.class, "/it/michelemasetto/telegram/bots/sqlclient"));
        map = new ConcurrentHashMap<>();
    }

    public Ability connect() {
        return Ability.builder()
                .name("connect")
                .info("connect to database")
                .input(3)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    long chatId = ctx.chatId();
                    if (!map.containsKey(chatId)) {
                        map.put(chatId, new SqlClientProperties());
                    }
                    SqlClientProperties prop = map.get(chatId);
                    prop.setUrl(ctx.firstArg());
                    prop.setUser(ctx.secondArg());
                    prop.setPassword(ctx.thirdArg());
                    silent.send(String.format("Connected to %s with user %s.", prop.getUrl(), prop.getUser()), ctx.chatId());
                })
                .build();
    }

    public Ability query() {
        return Ability.builder()
                .name("query")
                .info("execute query")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    try {
                        long chatId = ctx.chatId();
                        if (!map.containsKey(chatId)) {
                            silent.send("You must connect to a database first.", chatId);
                        } else {
                            SqlClientProperties prop = map.get(chatId);
                            String sql = String.join(" ", ctx.arguments());
                            try (Connection con = DriverManager.getConnection(prop.getUrl(), prop.getUser(), prop.getPassword());
                                 PreparedStatement ps = con.prepareStatement(sql);
                                 ResultSet rs = ps.executeQuery(sql)) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("columns", getColumns(rs));
                                map.put("data", getData(rs));
                                Template template = cfg.getTemplate("template.ftlh");
                                Writer writer = new StringWriter();
                                template.process(map, writer);
                                silent.execute(SendRichMessage.builder()
                                        .chatId(chatId)
                                        .richMessage(InputRichMessage.builder()
                                                .html(writer.toString())
                                                .build())
                                        .build());
                            } catch (SQLException e) {
                                silent.send(e.getMessage(), chatId);
                            }
                        }
                    } catch (Exception e) {
                        log.error("An error occurred during execution", e);
                    }
                })
                .build();
    }

    private List<String> getColumns(ResultSet rs) throws SQLException {
        return IntStream.range(1, rs.getMetaData().getColumnCount() + 1)
                .mapToObj(Util.throwingIntFunction(i -> rs.getMetaData().getColumnLabel(i)))
                .toList();
    }

    private List<List<Object>> getData(ResultSet rs) throws SQLException {
        List<List<Object>> data = new ArrayList<>();
        while (rs.next()) {
            data.add(IntStream.range(1, rs.getMetaData().getColumnCount() + 1)
                    .mapToObj(Util.throwingIntFunction(rs::getObject))
                    .toList());
        }
        return data;
    }
}
