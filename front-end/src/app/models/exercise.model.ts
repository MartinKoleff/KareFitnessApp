export interface Exercises{
  name:string;
  description:string;
  type:string;
}
export interface MuscleGroup{
  name:string;
  exercises:Exercises[];
}
export const biceps: MuscleGroup = {
  name: 'biceps',
  exercises: [
    {
      name: 'Bicep curl',
      description: 'Description for bicep curl exercise.',
      type: 'dumbell',
    },
    {
      name: 'Incline dumbell curl',
      description: 'Description for incline dumbell curl exercise.',
      type: 'dumbell',
    },
    {
      name: 'Hammer curl',
      description: 'Description for hammer curl exercise.',
      type: 'dumbell',
    },
    {
      name: 'Concentration curl',
      description: 'Description for concentration curl exercise.',
      type: 'dumbell',
    },
    {
      name: 'Barbell curl',
      description: 'Description for barbell curl exercise.',
      type: 'barbell',
    },
  ]
}
export const triceps: MuscleGroup = {
  name: 'triceps',
  exercises: [
    {
      name: 'Diamond push-up',
      description: 'Description for diamond Push',
      type: 'bodyweight',
    },
    {
      name: 'Triceps dips',
      description: 'Description for triceps Dips',
      type: 'bodyweight',
    },
    {
      name: 'Overhead triceps extensions',
      description: 'Description for overhead triceps extensions',
      type: 'dumbell',
    },
    {
      name: 'Rope pushdowns',
      description: 'Description for rope pushdowns',
      type: 'machine',
    },
    {
      name: 'Skull crushers',
      description: 'Description for Barbell Curl exercise.',
      type: 'barbell',
    },
  ]
}
export const legs: MuscleGroup = {
  name: 'legs',
  exercises: [
    {
      name: 'Squats',
      description: 'Description for squats',
      type: 'barbell',
    },
    {
      name: 'Romanian deadlift',
      description: 'Description for romanian deadlift',
      type: 'barbell',
    },
    {
      name: 'Bulgarian split-squats',
      description: 'Description for bulgarian split-squats',
      type: 'dumbell',
    },
    {
      name: 'Leg press',
      description: 'Description for leg press',
      type: 'machine',
    },
    {
      name: 'Hack suqat',
      description: 'Description for hack squats.',
      type: 'machine',
    },
  ]
}
export const back: MuscleGroup = {
  name: 'back',
  exercises: [
    {
      name: 'Pull-ups',
      description: 'Description for pull-ups',
      type: 'bodyweight',
    },
    {
      name: 'Rows',
      description: 'Description for Rows',
      type: 'barbell',
    },
    {
      name: 'Deadlifts',
      description: 'Description for deadlifts',
      type: 'barbell',
    },
    {
      name: 'One-arm dumbell rows',
      description: 'Description for one-arm dumbell row',
      type: 'dumbell',
    },
    {
      name: 'Landmine rows',
      description: 'Description for landmine rows',
      type: 'barbell',
    },
  ]
}
export const shoulders: MuscleGroup = {
  name: 'shoulders',
  exercises: [
    {
      name: 'Overhead press',
      description: 'Description for overhead press',
      type: 'barbell',
    },
    {
      name: 'Arnold press',
      description: 'Description for arnold press',
      type: 'dumbbell',
    },
    {
      name: 'Lateral raises',
      description: 'Description for lateral raises',
      type: 'dumbell',
    },
    {
      name: 'Front raises',
      description: 'Description for front raises',
      type: 'dumbell',
    },
    {
      name: 'Reverse flye',
      description: 'Description for reverse flye',
      type: 'machine',
    },
  ]
}
export const chest: MuscleGroup = {
  name: 'chest',
  exercises: [
    {
      name: 'Bench press',
      description: 'Description for bench press',
      type: 'barbell',
    },
    {
      name: 'Push-ups',
      description: 'Description for push-ups',
      type: 'bodyweight',
    },
    {
      name: 'Machine fly',
      description: 'Description for machine fly',
      type: 'machine',
    },
    {
      name: 'Incline dumbell press',
      description: 'Description for incline dumbell press',
      type: 'dumbell',
    },
    {
      name: 'Dips',
      description: 'Description for dips',
      type: 'bodyweight',
    },
  ]
}
export const cardio: MuscleGroup = {
  name: 'cardio',
  exercises: [
    {
      name: 'Running',
      description: 'Description for running',
      type: 'cardio',
    },
    {
      name: 'Jumping jacks',
      description: 'Description for jumping jacks',
      type: 'cardio',
    },
    {
      name: 'Lunges',
      description: 'Description for lunges',
      type: 'cardio',
    },
    {
      name: 'Cycling',
      description: 'Description for cycling',
      type: 'cardio',
    },
    {
      name: 'Squat jumps',
      description: 'Description for squat jumps',
      type: 'cardio',
    },
  ]
}
export const abs: MuscleGroup = {
  name: 'abs',
  exercises: [
    {
      name: 'Plank',
      description: 'Description for plank',
      type: 'bodyweight',
    },
    {
      name: 'Bicycle crunch',
      description: 'Description for bicycle crunch',
      type: 'bodyweight',
    },
    {
      name: 'Crunch',
      description: 'Description for crunch',
      type: 'bodyweight',
    },
    {
      name: 'Cable crunch',
      description: 'Description for cable crunch',
      type: 'machine',
    },
    {
      name: 'Leg raises',
      description: 'Description for leg raises',
      type: 'bodyweight',
    },
  ]
}
