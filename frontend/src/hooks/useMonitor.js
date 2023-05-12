import { api } from "../api"


export const useMonitor = () => {

    const getData = async () => {
        
        try {
            
            const {data, status } = await api.get('/monitoring')

            if( status === 200 ){
                return data;
            }else{
                console.log(data.msg);
            }
            
        } catch (error) {
            console.log(error)
        }


    }

    return {

        getData
    }
}