import './App.css';
import React, { Component } from 'react';
import axios from 'axios';
import DirectionsCarOutlinedIcon from '@material-ui/icons/DirectionsCarOutlined';

const url = 'http://172.23.40.140:8080/Backend/webresources/generic';
class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      parkingLots : [],
      submitData :{}
    }
    this.updateAvailability = this.updateAvailability.bind(this);
  }
  async componentDidMount() {
    await axios.get(url).then(res =>{
      console.log(res.data.parkingFloorA)
      this.setState({ parkingLots : res.data.parkingFloorA })
      //console.log()
    })
  }
   updateAvailability(index){
    let {parkingLots, submitData} = this.state;
    
    let config = {
      headers: {'Content-Type': 'application/json'}
    };
    //console.log
    if(parkingLots[index].availability){
     // console.log(submitData)
       submitData = { "id": parkingLots[index].slot , "availability": "false"}
       console.log(submitData)
    }
    else{
       submitData = {"id":parkingLots[index].slot, "availability": "true"}
      console.log(submitData)
    }
    axios.post(url, submitData, config).then(res => {
      console.log(res);
      this.setState({ submitData : {}});
       axios.get(url).then(res =>{
        console.log(res.data.parkingFloorA)
        this.setState({ parkingLots : res.data.parkingFloorA })
        //console.log()
      })
    }).catch(err => {
      console.log(err,"error in update")
    })

   
  }
  render() {
    return (
      <div>
  <div className = 'total'>Total Slots : {this.state.parkingLots.length}</div>
  
      <div className = 'block'>

        {this.state.parkingLots.map((item, index) => (
          <div key = { index }   className = {item.availability ? 'occupy' : 'release'} >
            
            <div className= 'slotNumber'>{item.slot}</div> 
            <div ><DirectionsCarOutlinedIcon className = 'carIcon' /></div>
            {item.availability ? <button onClick = {() => this.updateAvailability(index)}>Release</button> :
                    <button onClick = {() => this.updateAvailability(index)}>Occupy</button>}
             </div>
        ))
        }
      </div>
      </div>
    );
  }
}



export default App;