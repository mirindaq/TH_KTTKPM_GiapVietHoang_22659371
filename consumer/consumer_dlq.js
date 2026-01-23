const amqp = require("amqplib");

const RABBITMQ_URL = "amqp://user:password@rabbitmq:5672";
const QUEUE = "message_queue";
const DEAD_LETTER_QUEUE = "message_queue.dlq";

let channel;

async function connectWithRetry() {
  try {
    const conn = await amqp.connect(RABBITMQ_URL);
    channel = await conn.createChannel();

    await channel.assertQueue(DEAD_LETTER_QUEUE, { durable: true });

    await channel.assertQueue(QUEUE, {
      durable: true,
      deadLetterExchange: "",
      deadLetterRoutingKey: DEAD_LETTER_QUEUE,
    });

    channel.consume(
      QUEUE,
      async (msg) => {
        if (!msg) return;

        const body = msg.content.toString();

        try {
          const data = JSON.parse(body);

          if (!data.message) {
            throw new Error("Message is required");
          }

          await new Promise(resolve => setTimeout(resolve, 3000));

          channel.ack(msg);
        } catch (err) {
          channel.nack(msg, false, false);
        }
      },
      { noAck: false }
    );

  } catch (err) {
    setTimeout(connectWithRetry, 3000);
  }
}

connectWithRetry();
