import { api } from "../api"


export const useMonitor = () => {

    const getData = async () => {
        
        try {
            
            //const { status, data } = api.get('/monitor')
            const { status, data } = {
                status: 200,
                data: {
                    events2hs: 45,
                    votes2hs: 10
                }
            }

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