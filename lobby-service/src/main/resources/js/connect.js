// const socket = new SockJS('/ws'); // URL WebSocket
// const stompClient = Stomp.over(socket);
//
// stompClient.connect({}, function () {
//     console.log('Connected to WebSocket');
//
//     // Подписка на события в конкретном лобби
//     stompClient.subscribe('/topic/lobby/1', function (message) {
//         console.log('Message received:', JSON.parse(message.body));
//     });
//
//     // Отправка действия игрока
//     stompClient.send('/app/lobby/action', {}, JSON.stringify({
//         lobbyId: 1,
//         playerId: 123,
//         actionType: 'JOIN',
//         payload: 'Player 123 joined the lobby'
//     }));
// });
