package main.frame.lobbyservice.model;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "lobby_snapshots")
//public class LobbySnapshot {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String lobbyStateJson; // JSON для хранения состояния лобби
//
//    public LobbySnapshot(Lobby lobby) {
//        this.lobbyStateJson = convertLobbyToJson(lobby);
//    }
//
//    public Lobby toLobby() {
//        return convertJsonToLobby(lobbyStateJson);
//    }
//
//    private String convertLobbyToJson(Lobby lobby) {
//        // Сериализация лобби в JSON
//        return "{}"; // Упростим
//    }
//
//    private Lobby convertJsonToLobby(String json) {
//        // Десериализация JSON обратно в Lobby
//        return new Lobby();
//    }
//}
