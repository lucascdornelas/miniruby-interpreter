#ifndef COMMAND_H
#define COMMAND_H

class Command {
public:
    virtual ~Command() {}

    int line() const { return m_line; }
    virtual void execute() = 0;

protected:
    explicit Command(int line) : m_line(line) {}

private:
    int m_line;

};

#endif
