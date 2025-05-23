CREATE TABLE IF NOT EXISTS users (
          id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
          email VARCHAR(255) NOT NULL UNIQUE,
          fullname VARCHAR(255) NOT NULL
        );

        CREATE TABLE IF NOT EXISTS requests (
          id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
          description VARCHAR(255) NOT NULL,
          created_date timestamp NOT NULL,
          user_id BIGINT NOT NULL,
          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
        );

        CREATE TABLE IF NOT EXISTS items (
          id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
          name VARCHAR(255) NOT NULL,
          description VARCHAR(255) NOT NULL,
          available BOOLEAN NOT NULL,
          user_id BIGINT NOT NULL,
          request_id BIGINT,
          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
          FOREIGN KEY (request_id) REFERENCES requests(id) ON DELETE CASCADE
        );

        CREATE TABLE IF NOT EXISTS bookings (
          id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
          start_date timestamp NOT NULL,
          end_date timestamp NOT NULL,
          item_id BIGINT NOT NULL,
          user_id BIGINT NOT NULL,
          status VARCHAR(50) NOT NULL,
          FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
        );
		
		CREATE TABLE IF NOT EXISTS comments (
          id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
          text VARCHAR(255) NOT NULL,
          item_id BIGINT NOT NULL,
          user_id BIGINT NOT NULL,
          created timestamp NOT NULL,
          FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
        );