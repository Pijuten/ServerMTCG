    package at.fhtw.mtcg.model;

    import com.fasterxml.jackson.annotation.JsonAlias;
    import lombok.Getter;
    import lombok.Setter;

    @Getter @Setter
    public class User {
        @JsonAlias({"Username"})
        private String username;
        @JsonAlias({"Password"})
        private String password;
        @JsonAlias({"Hashedpassword"})
        private byte[] hashedPassword;
        @JsonAlias({"Token"})
        private String token;
        @JsonAlias({"Currency"})
        private int currency;
        @JsonAlias({"Score"})
        private int score;
        @JsonAlias({"Win"})
        private int win;
        @JsonAlias({"Draw"})
        private int draw;
        @JsonAlias({"Loss"})
        private int loss;
        @JsonAlias({"Name"})
        private String displayName;
        @JsonAlias({"Bio"})
        private String bio;
        @JsonAlias({"Image"})
        private String profileImage;
        @JsonAlias({"Salt"})
        private byte[] salt;
        public User() {}
        public User(String username,
                    String password) {
            this.username = username;
            this.password = password;
        }
        public User(String displayName,
                    String bio,
                    String profileImage
                    ) {
            this.displayName = displayName;
            this.bio = bio;
            this.profileImage = profileImage;
        }
        public User(
                String username,
                byte[] hashedPassword,
                String token,
                int currency,
                int score,
                int win,
                int draw,
                int loss,
                String displayName,
                String bio,
                String profileImage,
                byte[] salt) {
            this.username = username;
            this.hashedPassword = hashedPassword;
            this.token = token;
            this.currency = currency;
            this.score = score;
            this.win = win;
            this.draw = draw;
            this.loss = loss;
            this.displayName = displayName;
            this.bio = bio;
            this.profileImage = profileImage;
            this.salt = salt;
        }
    }