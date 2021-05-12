extern "C"
__global__ void compute(
    unsigned int width,
    unsigned int height,
    unsigned char* current,
    unsigned char* next)
{

    int column = blockIdx.x * blockDim.x + threadIdx.x;
    int row = blockIdx.y * blockDim.y + threadIdx.y;

    if (column < width && row < height)
    {
        int cells = 0;
        for (int nRow = row - 1; nRow <= row + 1; nRow++) {
            if (0 <= nRow && nRow < height) {

                for (int nColumn = column - 1; nColumn <= column + 1; nColumn++) {
                    if (0 <= nColumn && nColumn < width) {

                        cells += current[nRow * width + nColumn];

                    }
                }
            }
        }

        //recycling variable
        column = (row * width) + column;

        switch (cells)
        {
        case 3:
            next[column] = 1;
            break;
        case 4:
            next[column] = current[column];
            break;
        default:
            next[column] = 0;
        }
    }
}