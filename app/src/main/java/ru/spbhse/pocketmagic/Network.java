package ru.spbhse.pocketmagic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class Network {

    public class CallbackHandler extends RoomStatusUpdateCallback {

        @Override
        public void onRoomConnecting(@Nullable Room room) {
            Log.d("Pocket Magic", "Successful connection to room");
        }

        @Override
        public void onRoomAutoMatching(@Nullable Room room) {
            Log.d("Pocket Magic", "New room found");
        }

        @Override
        public void onPeerInvitedToRoom(@Nullable Room room, @NonNull List<String> list) {
            Log.d("Pocket Magic", "Receive an invitation to a new room");
        }

        @Override
        public void onPeerDeclined(@Nullable Room room, @NonNull List<String> list) {
            Log.d("Pocket Magic", "Peer connection declined");
        }

        @Override
        public void onPeerJoined(@Nullable Room room, @NonNull List<String> list) {
            Log.d("Pocket Magic", "Joined with peer connection");
        }

        @Override
        public void onPeerLeft(@Nullable Room room, @NonNull List<String> list) {
            Log.d("Pocket Magic", "Left from peer connection");
            Network.this.leaveRoom();
        }

        @Override
        public void onConnectedToRoom(@Nullable Room room) {
            Log.d("Pocket Magic", "Connected to a new room");
        }

        @Override
        public void onDisconnectedFromRoom(@Nullable Room room) {
            Log.d("Pocket Magic", "Disconnected from a room");
            Network.this.leaveRoom();
        }

        @Override
        public void onPeersConnected(@Nullable Room room, @NonNull List<String> list) {
            Log.d("Pocket Magic", "Connected with a peer connection");
        }

        @Override
        public void onPeersDisconnected(@Nullable Room room, @NonNull List<String> list) {
            Log.d("Pocket Magic", "Disconnected from a peer connection");
            Network.this.leaveRoom();
        }

        @Override
        public void onP2PConnected(@NonNull String s) {
            Log.d("Pocket Magic", "P2P connected");
        }

        @Override
        public void onP2PDisconnected(@NonNull String s) {
            Log.d("Pocket Magic", "P2P disconnected");
            Network.this.leaveRoom();
        }
    }

    public class MessageListener implements OnRealTimeMessageReceivedListener {

        @Override
        public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
            Log.d("Pocket Magic","Received message");
            byte[] buf = realTimeMessage.getMessageData();

            try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(buf))) {
                NetworkController.receiveSpell(in.readInt());
            } catch (IOException e) {
                Log.wtf("Pocket Magic", "Can't create DataInputStream!");
                e.printStackTrace();
            }
        }
    }

    public class CallbackUpdater extends RoomUpdateCallback {

        @Override
        public void onRoomCreated(int i, @Nullable Room room) {
            Log.d("Pocket Magic", "Room was created");
        }

        @Override
        public void onJoinedRoom(int i, @Nullable Room room) {
            Log.d("Pocket Magic", "Joined to room");
        }

        @Override
        public void onLeftRoom(int i, @NonNull String s) {
            Log.d("Pocket Magic", "Left from room");
            NetworkController.finishGame();
        }

        @Override
        public void onRoomConnected(int i, @Nullable Room room) {
            if (room == null) {
                Log.wtf("Pocket Magic", "Null pointer as Room!");
                return;
            }
            Log.d("Pocket Magic", "Connected to room");

            Network.this.room = room;
            if (i == GamesCallbackStatusCodes.OK) {
                Log.d("Pocket Magic", "Connected");
            } else {
                Log.e("Pocket Magic", "Error while connecting");
                return;
            }

            Games.getPlayersClient(NetworkController.getContext(), account)
                    .getCurrentPlayerId()
                    .addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String myPlayerId) {
                            Network.this.playerID = Network.this.room.getParticipantId(myPlayerId);
                            Log.d("Pocket Magic", "Received playerID");
                            NetworkController.startGame();
                        }
                    });
        }
    }

    private RoomConfig roomConfig;
    private Room room;
    private GoogleSignInAccount account;
    private RealTimeMultiplayerClient client;
    private String playerID;

    public Network(GoogleSignInAccount account) {
        this.account = account;
        client = Games.getRealTimeMultiplayerClient(NetworkController.getContext(),
                this.account);
    }

    public void findAndStartGame() {
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1, 0);

        roomConfig = RoomConfig.builder(this.new CallbackUpdater())
                .setOnMessageReceivedListener(this.new MessageListener())
                .setRoomStatusUpdateCallback(this.new CallbackHandler())
                .setAutoMatchCriteria(autoMatchCriteria)
                .build();

        Log.wtf("Pocket Magic", "Prepared for creating a room");
        client.create(roomConfig);
        Log.wtf("Pocket Magic", "Room created");
    }

    public void leaveRoom() {
        if (room != null) {
            Games.getRealTimeMultiplayerClient(NetworkController.getContext(),
                    account).leave(roomConfig, room.getRoomId());
            room = null;
        }
    }

    public void sendMessage(byte[] message) {
        if (playerID == null || room == null) {
            Log.e("Pocket Magic", "Cannot send message before initialization");
            return;
        }

        for (String id: room.getParticipantIds()) {
            if (!id.equals(playerID)) {
                client.sendReliableMessage(
                        message, room.getRoomId(),
                        id,
                        new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
                            @Override
                            public void onRealTimeMessageSent(int i, int i1, String s) {
                                /* Do nothing */
                            }
                        });
            }
        }
    }
}
