package it.michelemasetto.telegram.bots.sqlclient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SqlClientProperties {

    private String url;

    private String user;

    private String password;
}
