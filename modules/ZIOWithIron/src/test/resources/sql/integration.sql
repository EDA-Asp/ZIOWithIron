CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    age INTEGER NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);