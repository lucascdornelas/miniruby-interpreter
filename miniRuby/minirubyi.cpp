#include <iostream>
#include <cstdlib>

int main(int argc, char *argv[])
{
    if (argc != 2)
    {
        std::cout << "Usage: " << argv[0] << " [MiniRuby program]" << std::endl;
        exit(1);
    }

    return 0;
}